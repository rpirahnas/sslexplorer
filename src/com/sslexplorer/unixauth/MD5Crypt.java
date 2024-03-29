/*

 Based on the work of Jonathan Abbey
 Adopted for Director by Thomas Aeby, 2004

 MD5Crypt.java

 Created: 3 November 1999

 Java Code By: Jonathan Abbey, jonabbey@arlut.utexas.edu
 Original C Version:
 ----------------------------------------------------------------------------
 "THE BEER-WARE LICENSE" (Revision 42):
 <phk@login.dknet.dk> wrote this file.  As long as you retain this notice you
 can do whatever you want with this stuff. If we meet some day, and you think
 this stuff is worth it, you can buy me a beer in return.   Poul-Henning Kamp
 ----------------------------------------------------------------------------

 -----------------------------------------------------------------------
 
 Ganymede Directory Management System

 Copyright (C) 1996, 1997, 1998, 1999  The University of Texas at Austin.

 Contact information

 Author Email: ganymede_author@arlut.utexas.edu
 Email mailing list: ganymede@arlut.utexas.edu

 US Mail:

 Computer Science Division
 Applied Research Laboratories
 The University of Texas at Austin
 PO Box 8029, Austin TX 78713-8029

 Telephone: (512) 835-3200

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 */

package com.sslexplorer.unixauth;

import com.maverick.crypto.digests.MD5Digest;


/*------------------------------------------------------------------------------
 class
 MD5Crypt

 ------------------------------------------------------------------------------*/

/**
 * <p>
 * This class defines a method,
 * {@link MD5Crypt#crypt(java.lang.String, java.lang.String) crypt()}, which
 * takes a password and a salt string and generates an
 * OpenBSD/FreeBSD/Linux-compatible md5-encoded password entry.
 * </p>
 * 
 * <p>
 * Created: 3 November 1999
 * </p>
 * <p>
 * Release: $Name: SSLEXPLORER_1_0_0_BRANCH $
 * </p>
 * <p>
 * Java Code By: Jonathan Abbey, jonabbey@arlut.utexas.edu
 * </p>
 * <p>
 * Original C Version:
 * 
 * <pre>
 * 
 *  ----------------------------------------------------------------------------
 *  &quot;THE BEER-WARE LICENSE&quot; (Revision 42):
 *  &lt;phk@login.dknet.dk&gt; wrote this file.  As long as you retain this notice you
 *  can do whatever you want with this stuff. If we meet some day, and you think
 *  this stuff is worth it, you can buy me a beer in return.   Poul-Henning Kamp
 *  ----------------------------------------------------------------------------
 *  
 * </pre>
 * 
 * </p>
 */

public final class MD5Crypt {

    /**
     * 
     * Command line test rig.
     *  
     */

    static public void main(String argv[]) {
        if ((argv.length < 1) || (argv.length > 2)) {
            System.err.println("Usage: MD5Crypt password salt");
            System.exit(1);
        }

        if (argv.length == 2) {
            System.err.println(MD5Crypt.crypt(argv[0], argv[1]));
        } else {
            System.err.println(MD5Crypt.crypt(argv[0]));
        }

        System.exit(0);
    }

    static private final String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    static private final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    static private final String to64(long v, int size) {
        StringBuffer result = new StringBuffer();

        while (--size >= 0) {
            result.append(itoa64.charAt((int) (v & 0x3f)));
            v >>>= 6;
        }

        return result.toString();
    }

    static private final void clearbits(byte bits[]) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = 0;
        }
    }

    /**
     * convert an encoded unsigned byte value into a int with the unsigned
     * value.
     */

    static private final int bytes2u(byte inp) {
        return (int) inp & 0xff;
    }

    /**
     * <p>
     * This method actually generates a OpenBSD/FreeBSD/Linux PAM compatible
     * md5-encoded password hash from a plaintext password and a salt.
     * </p>
     * 
     * <p>
     * The resulting string will be in the form '$1$&lt;salt&gt;$&lt;hashed
     * mess&gt;
     * </p>
     * 
     * @param password
     *            Plaintext password
     * 
     * @return An OpenBSD/FreeBSD/Linux-compatible md5-hashed password field.
     */

    static public final String crypt(String password) {
        StringBuffer salt = new StringBuffer();
        java.util.Random randgen = new java.util.Random();

        /* -- */

        while (salt.length() < 8) {
            int index = (int) (randgen.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.substring(index, index + 1));
        }

        return MD5Crypt.crypt(password, salt.toString());
    }

    /**
     * <p>
     * This method actually generates a OpenBSD/FreeBSD/Linux PAM compatible
     * md5-encoded password hash from a plaintext password and a salt.
     * </p>
     * 
     * <p>
     * The resulting string will be in the form '$1$&lt;salt&gt;$&lt;hashed
     * mess&gt;
     * </p>
     * 
     * @param password
     *            Plaintext password
     * @param salt
     *            A short string to use to randomize md5. May start with $1$,
     *            which will be ignored. It is explicitly permitted to pass a
     *            pre-existing MD5Crypt'ed password entry as the salt. crypt()
     *            will strip the salt chars out properly.
     * 
     * @return An OpenBSD/FreeBSD/Linux-compatible md5-hashed password field.
     */

    static public final String crypt(String password, String salt) {
        /*
         * This string is magic for this algorithm. Having it this way, we can
         * get get better later on
         */

        String magic = "$1$";
        byte finalState[];
        MD5Digest ctx, ctx1;
        long l;

        /* -- */

        /* Refine the Salt first */

        /* If it starts with the magic string, then skip that */

        if (salt.startsWith(magic)) {
            salt = salt.substring(magic.length());
        }

        /* It stops at the first '$', max 8 chars */

        if (salt.indexOf('$') != -1) {
            salt = salt.substring(0, salt.indexOf('$'));
        }

        if (salt.length() > 8) {
            salt = salt.substring(0, 8);
        }

        ctx = new MD5Digest();

        write(ctx, password.getBytes()); // The password first, since that is
                                         // what is most unknown
        write(ctx, magic.getBytes()); // Then our magic string
        write(ctx, salt.getBytes()); // Then the raw salt

        /* Then just as many characters of the MD5(pw,salt,pw) */

        ctx1 = new MD5Digest();
        write(ctx1, password.getBytes());
        write(ctx1, salt.getBytes());
        write(ctx1, password.getBytes());


        finalState = new byte[ctx1.getDigestSize()];
        ctx1.doFinal(finalState, 0);

        for (int pl = password.length(); pl > 0; pl -= 16) {
            for (int i = 0; i < (pl > 16 ? 16 : pl); i++)
                ctx.update(finalState[i]);
        }

        /*
         * the original code claimed that finalState was being cleared to keep
         * dangerous bits out of memory, but doing this is also required in
         * order to get the right output.
         */

        clearbits(finalState);

        /* Then something really weird... */

        for (int i = password.length(); i != 0; i >>>= 1) {
            if ((i & 1) != 0) {
                ctx.update(finalState[0]);
            } else {
                ctx.update(password.getBytes()[0]);
            }
        }

        finalState = new byte[ctx.getDigestSize()];
        ctx.doFinal(finalState, 0);

        /*
         * and now, just to make sure things don't run too fast On a 60 Mhz
         * Pentium this takes 34 msec, so you would need 30 seconds to build a
         * 1000 entry dictionary...
         * 
         * (The above timings from the C version)
         */

        for (int i = 0; i < 1000; i++) {
            ctx1 = new MD5Digest();

            if ((i & 1) != 0) {
                write(ctx1, password.getBytes());
            } else {
                for (int c = 0; c < 16; c++)
                    ctx1.update(finalState[c]);
            }

            if ((i % 3) != 0) {
                write(ctx1, salt.getBytes());
            }

            if ((i % 7) != 0) {
                write(ctx1, password.getBytes());
            }

            if ((i & 1) != 0) {
                for (int c = 0; c < 16; c++)
                    ctx1.update(finalState[c]);
            } else {
                write(ctx1, password.getBytes());
            }


            finalState = new byte[ctx.getDigestSize()];
            ctx1.doFinal(finalState, 0);
        }

        /* Now make the output string */

        StringBuffer result = new StringBuffer();

        result.append(magic);
        result.append(salt);
        result.append("$");

        l = (bytes2u(finalState[0]) << 16) | (bytes2u(finalState[6]) << 8) | bytes2u(finalState[12]);
        result.append(to64(l, 4));

        l = (bytes2u(finalState[1]) << 16) | (bytes2u(finalState[7]) << 8) | bytes2u(finalState[13]);
        result.append(to64(l, 4));

        l = (bytes2u(finalState[2]) << 16) | (bytes2u(finalState[8]) << 8) | bytes2u(finalState[14]);
        result.append(to64(l, 4));

        l = (bytes2u(finalState[3]) << 16) | (bytes2u(finalState[9]) << 8) | bytes2u(finalState[15]);
        result.append(to64(l, 4));

        l = (bytes2u(finalState[4]) << 16) | (bytes2u(finalState[10]) << 8) | bytes2u(finalState[5]);
        result.append(to64(l, 4));

        l = bytes2u(finalState[11]);
        result.append(to64(l, 2));

        /* Don't leave anything around in vm they could use. */
        clearbits(finalState);

        return result.toString();
    }
    
    static void write(MD5Digest digest, byte[] arr) {
        digest.update(arr, 0, arr.length);
    }
}