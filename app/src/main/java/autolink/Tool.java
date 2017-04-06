package autolink;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Tool {
	public static final int REC_DATA = 0x01;
	
	/**
	 * У??????????
	 * @param data
	 * @return
	 */
	public static boolean checkData(byte[] data){
		System.out.println("data---->"+bytesToHexString(data));
		int sum = 0;
	    for(int i=1;i<data.length-1;i++){
	    	sum += data[i];
	    }
	    System.out.println("sum&0xff---------->"+(sum & 0xff));
	    return (sum&0xff) == data[data.length-1];
	}

	
	/**
	 * ????????SSID?б????
	 */
	public static ArrayList<Item> decode_81_data(byte[] data) {
		ArrayList<Item> items = new ArrayList<Item>();
		byte[] ssidData = new byte[data.length - 6];
		System.arraycopy(data, 5, ssidData, 0, ssidData.length);
		int last = 0 ;
		for(int i=0;i<ssidData.length-1;i++){
			//???2????????????飬?????0d0a(\r\n)
			byte[] two = new byte[2];
			two[0] = ssidData[i] ;
			two[1] = ssidData[i+1];
			String zdza = new String(two);
			if(zdza.equals("\r\n")){//?????0d0a???????Э??????????????????
				Item item = new Item();
				byte[] name = new byte[i-2-last];//??????????2?????
				System.arraycopy(ssidData, last, name, 0, name.length);
                item.setName(new String(name).trim());
                int dbm = ssidData[i-1]&0xff;//??????????
                item.setDbm(dbm);
                items.add(item);
                last = i+2;//???last??????????????????ssid??????????
			}
		}
		
		return items;
	}

	public static int[] decode_82_data(byte[] data) {
		int[] values = new int[2];
		values[0] = data[4] & 0xff;
		values[1] = data[5] & 0xff;
		return values;
	}

	/**
	 * ?????趨ssid?????????
	 * 
	 * @param ssid
	 * @param pasd
	 * @param index
	 *            ssid????????? ?????????????0????
	 * @return
	 */
	public static byte[] generate_02_data(String ssid, String pasd, int index) {
		try {
			String str = ssid + "\r\n" + pasd;
			byte[] strBytes = str.getBytes("utf-8");
			byte[] data = new byte[1 + 1 + strBytes.length];
			data[0] = 0x02;
			data[1] = (byte) (index & 0xff);
			System.arraycopy(strBytes, 0, data, 2, strBytes.length);
			return generateCmd(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public static byte[] generateCmd(byte[] key) {
		// ????Э?飺???1 + ???? 2 + ???? + ????+ У??1
		int length = 4 + key.length;
		byte[] cmd = new byte[length];
		cmd[0] = (byte) 0xff;

		byte[] lengthBytes = int2byte(key.length);
		cmd[1] = lengthBytes[1];
		cmd[2] = lengthBytes[0];

		// У??λ???
		cmd[length - 1] = (byte) (cmd[1] + cmd[2]);
		for (int i = 0; i < key.length; i++) {
			cmd[i + 3] = key[i];
			cmd[length - 1] += key[i];
		}
		return cmd;
	}

	/**
	 * int ?byte[]
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);// ???λ
		targets[1] = (byte) ((res >> 8) & 0xff);// ?ε?λ
		targets[2] = (byte) ((res >> 16) & 0xff);// ?θ?λ
		targets[3] = (byte) (res >>> 24);// ???λ,??????????
		return targets;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv+" ");
		}
		return stringBuilder.toString();
	}

	public static int byteToInt2(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < 4; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
}
