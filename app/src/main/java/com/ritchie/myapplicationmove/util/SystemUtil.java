package android.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SystemUtil {
	private static final String TAG = "SystemUtil";
	public static int execShellCmdForStatue(String command) {
		int status = -1;
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = "";
			while((s = bufferedReader.readLine()) != null){
				Log.d(TAG, " >>>> " + s);
			}
			status = p.waitFor();
			Log.d(TAG, " ________________----------- command: " + command + "    status = " + status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;

	}

	public static String execShellCmd(String command) {
		android.util.Log.d("liujunjie","结果是"+"command"+command);
		String result = "";
		Log.i("execShellCmd", command);
		try {
			Process process = Runtime.getRuntime().exec(command + "\n");
			DataOutputStream stdin = new DataOutputStream(
					process.getOutputStream());
			DataInputStream stdout = new DataInputStream(
					process.getInputStream());
			DataInputStream stderr = new DataInputStream(
					process.getErrorStream());
			String line;
			while ((line = stdout.readLine()) != null) {
				result += line + "\n";
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 1);
			}
			while ((line = stderr.readLine()) != null) {
				Log.e("EXEC", line);
			}
			process.waitFor();
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

	public static String execRootCmd(String command) {

		String result = execShellCmd("su root " + command + "\n");


		return result;
	}

	public static String execScriptCmd(String command, String path, boolean root) {
		File tempFile = null;
		String result = "";
		Log.i("execScriptCmd", command);
		try {
			tempFile = new File(path);
			tempFile.deleteOnExit();
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(tempFile)));
			br.write("#!/system/bin/sh\n");
			br.write(command);
			br.close();
			SystemUtil.execShellCmd("su root chmod 777 "
					+ tempFile.getAbsolutePath());
			result = SystemUtil.execShellCmd((root ? "su root " : "")
					+ tempFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		return result;
	}

	public static boolean killProcessByPath(String exePath) {
		File dir = new File("/proc/");
		String[] files = dir.list();
		int pid = -1;
		for (String path : files) {
			File file = new File("/proc/" + path + "/cmdline");
			if (file.exists()) {
				String cmdline = execShellCmd("cat " + file.getAbsolutePath());
				if (cmdline.startsWith(exePath)) {
					try {
						pid = Integer.parseInt(path);
						break;
					} catch (Exception e) {
						break;
					}
				}
			}
		}

		if (pid >= 0) {
			
			execShellCmd("su root kill " + pid);

			return true;
		}
		return false;
	}

}
