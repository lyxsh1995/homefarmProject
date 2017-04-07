package autolink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import diyikeji.homefarm.R;


/**
 * @author usr_liujinqi
 */
public class MainActivity extends Activity implements OnClickListener
{
	private EditText etSsid;
	private EditText etPasd;
	private EditText etPort;
	private WifiManager.MulticastLock lock;
	private SearchSSID searchSSID;
	private SendMsgThread smt;

	private ProgressDialog dialog;

	public final int RESQEST_SSID_LIST = 1;

	// ???ssid??????
	private final byte[] searchCode = new byte[] {(byte) 0xff, 0x00, 0x01,
			0x01, 0x02 };

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Tool.REC_DATA:// ???????????????
				byte[] data = (byte[]) msg.obj;
				Tool.bytesToHexString(data);
				decodeData(data);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		WifiManager manager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
//		lock = manager.createMulticastLock("fawifi");
//		lock.acquire();

		etSsid = (EditText) findViewById(R.id.et_ssid);
		etPasd = (EditText) findViewById(R.id.et_pasd);
		etPort = (EditText) findViewById(R.id.et_port);

		dialog = new ProgressDialog(this);
		dialog.setMessage("Search...");

		searchSSID = new SearchSSID(handler);
		searchSSID.start();

		smt = new SendMsgThread(searchSSID);
		smt.start();
	}

	@Override
	public void onClick(View v) {
		
		String port = etPort.getText().toString();
		if (TextUtils.isEmpty(port)) {
			UIUtil.toastShow(this, "please input port");
			return;
		}

		int targetPort = Integer.parseInt(port);

		if (targetPort < 0 || targetPort > 65535) {
			UIUtil.toastShow(this, "please input a right port ");
			return;
		}
		searchSSID.setTargetPort(Integer.parseInt(port));
		
		if (v.getId() == R.id.btn_search) {
		
			dialog.show();
			// ????????SSID???
			smt.putMsg(searchCode);
		
			dismiss();
		}

		if (v.getId() == R.id.btn_ok) {
			String ssid = etSsid.getText().toString();
			String pasd = etPasd.getText().toString();
			
			if (TextUtils.isEmpty(ssid)) {
				UIUtil.toastShow(this, "please input ssid");
				return;
			}

			if (TextUtils.isEmpty(pasd)) {
				// UIUtil.toastShow(this,"please input pasd");
				pasd = "";
				// return;
			}

			/**
			 * 48899????C32x????????????????AT?????? 49000??????C32x????????WIFI??????
			 * 1902?????????????????????
			 */
			searchSSID.setTargetPort(Integer.parseInt(port));

			byte[] data = Tool.generate_02_data(ssid, pasd, 0);
			smt.putMsg(data);
		}
	}

	/**
	 * ????????
	 * 
	 * @param data
	 */
	private void decodeData(byte[] data) {
		if ((data[0] & 0xff) != 0xff)// ?????????????????0xff???,???????
			return;
		switch (data[3] & 0xff) {
		case 0x81:// ??????????????
			dialog.dismiss();
			ArrayList<Item> ssids = Tool.decode_81_data(data);
			if (ssids.size() != 0) {
				Intent intent = new Intent(MainActivity.this, SsidListAct.class);
				intent.putExtra("ssids", ssids);
				startActivityForResult(intent, RESQEST_SSID_LIST);
			}
			break;
		case 0x82:// ?????????
			int[] values = Tool.decode_82_data(data);
			if (values[0] == 0)
				UIUtil.toastShow(this, R.string.no_ssid);
			else if (values[1] == 0)
				UIUtil.toastShow(this, R.string.error_pasd_length);
			else if (values[0] == 1 && values[1] == 1)
				UIUtil.toastShow(this, R.string.confing_end);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESQEST_SSID_LIST && data != null) {
			String ssid = data.getStringExtra("ssid");
			etSsid.setText(ssid);
		}
	}
	
	
	private void dismiss(){
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}, 3000);
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ???????
//		lock.release();
		smt.setSend(false);
		searchSSID.setReceive(false);
		searchSSID.close();
	}

	/**
	 * ??????????????????????????????????putMsg(byte[] data)????
	 * 
	 * @author usr_liujinqi
	 * 
	 */
	private class SendMsgThread extends Thread
	{
		// ????????????
		private Queue<byte[]> sendMsgQuene = new LinkedList<byte[]>();
		// ????????
		private boolean send = true;

		private SearchSSID ss;

		public SendMsgThread(SearchSSID ss) {
			this.ss = ss;
		}

		public synchronized void putMsg(byte[] msg) {
			// ???????
			if (sendMsgQuene.size() == 0)
				notify();
			sendMsgQuene.offer(msg);
		}

		public void run() {
			synchronized (this) {
				while (send) {
					// ?????????????????????????
					while (sendMsgQuene.size() > 0) {
						byte[] msg = sendMsgQuene.poll();
						if (ss != null)
							ss.sendMsg(msg);
					}
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public void setSend(boolean send) {
			this.send = send;
		}
	}
}
