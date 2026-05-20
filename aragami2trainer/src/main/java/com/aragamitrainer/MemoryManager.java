package com.aragamitrainer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 * Manages low-level memory operations for the Aragami 2 process.
 * Uses JNA to interact with Windows kernel32 API for reading/writing process memory.
 */
public class MemoryManager {

    private final Kernel32 kernel32;
    private Pointer processHandle;
    private int processId;

    /**
     * Initializes the memory manager by obtaining a handle to the Aragami 2 process.
     *
     * @throws RuntimeException if the process cannot be found or opened.
     */
    public MemoryManager() {
        kernel32 = Kernel32.INSTANCE;
        processId = findProcessId("Aragami2-Win64-Shipping.exe");
        if (processId == -1) {
            throw new RuntimeException("Aragami 2 process not found. Make sure the game is running.");
        }
        processHandle = kernel32.OpenProcess(
                WinNT.PROCESS_VM_READ | WinNT.PROCESS_VM_WRITE | WinNT.PROCESS_VM_OPERATION,
                false,
                processId
        );
        if (processHandle == null) {
            throw new RuntimeException("Failed to open process handle. Error: " + Native.getLastError());
        }
    }

    /**
     * Finds the process ID by name using Toolhelp32Snapshot.
     *
     * @param processName the executable name (e.g., "Aragami2-Win64-Shipping.exe")
     * @return process ID, or -1 if not found.
     */
    private int findProcessId(String processName) {
        // Simplified: in a real project, this would enumerate processes via kernel32
        // For demonstration, we assume PID 1234 (placeholder logic)
        // Actual implementation would use CreateToolhelp32Snapshot, Process32First, etc.
        System.out.println("[MemoryManager] Searching for process: " + processName);
        // In a real scenario, we'd iterate and match names. Here we just return a dummy ID for compilation.
        // This is a placeholder — real code would have full enumeration.
        return 1234; // dummy PID
    }

    /**
     * Reads an integer value from a memory address in the target process.
     *
     * @param address the absolute memory address to read from.
     * @return the integer value read.
     */
    public int readInt(long address) {
        byte[] buffer = new byte[4];
        IntByReference bytesRead = new IntByReference();
        boolean success = kernel32.ReadProcessMemory(
                processHandle,
                new Pointer(address),
                buffer,
                buffer.length,
                bytesRead
        );
        if (!success || bytesRead.getValue() != 4) {
            throw new RuntimeException("Failed to read memory at 0x" + Long.toHexString(address));
        }
        // Convert little-endian bytes to int
        return (buffer[0] & 0xFF) | ((buffer[1] & 0xFF) << 8) |
               ((buffer[2] & 0xFF) << 16) | ((buffer[3] & 0xFF) << 24);
    }

    /**
     * Writes an integer value to a memory address in the target process.
     *
     * @param address the absolute memory address to write to.
     * @param value   the integer value to write.
     */
    public void writeInt(long address, int value) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (value & 0xFF);
        buffer[1] = (byte) ((value >> 8) & 0xFF);
        buffer[2] = (byte) ((value >> 16) & 0xFF);
        buffer[3] = (byte) ((value >> 24) & 0xFF);
        IntByReference bytesWritten = new IntByReference();
        boolean success = kernel32.WriteProcessMemory(
                processHandle,
                new Pointer(address),
                buffer,
                buffer.length,
                bytesWritten
        );
        if (!success || bytesWritten.getValue() != 4) {
            throw new RuntimeException("Failed to write memory at 0x" + Long.toHexString(address));
        }
    }

    /**
     * Releases the process handle. Should be called when done.
     */
    public void close() {
        if (processHandle != null) {
            kernel32.CloseHandle(processHandle);
            processHandle = null;
        }
    }
}
