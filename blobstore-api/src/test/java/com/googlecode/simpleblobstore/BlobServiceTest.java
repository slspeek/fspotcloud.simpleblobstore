package com.googlecode.simpleblobstore;


import com.google.guiceberry.junit4.GuiceBerryRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BlobServiceTest {

    @Rule
    public GuiceBerryRule guiceBerry = new GuiceBerryRule(PlaceHolderGuiceBerryEnv.class);

    @Inject
    BlobService service;

    final private byte[] data;

    public BlobServiceTest(byte[] data) {
        this.data = data;
    }

    @Test
    public void testDelete() throws Exception {
        BlobKey key = saveInBlobstore(data);
        byte[] retrieved = null;

        retrieved = loadBytes(key);
        assertNotNull(retrieved);

        service.delete(key);
        retrieved = loadBytes(key);
        assertNull(retrieved);
    }

    private BlobKey saveInBlobstore(byte[] data) {
        return service.save("img/jpeg", data);
    }

    private byte[] loadBytes(BlobKey key) {
        return service.fetchData(key);
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        BlobKey key = saveInBlobstore(data);
        byte[] retrieved = null;

        retrieved = loadBytes(key);
        assertNotNull(retrieved);
        assertEquals(data.length, retrieved.length);
        assertTrue(Arrays.equals(data, retrieved));
    }

    public void testGetInfo() throws Exception {
        BlobKey key = saveInBlobstore(data);
        assertEquals(data.length, service.getInfo(key).getLength());
    }

    @Parameterized.Parameters

    public static Collection<Object[]> configs() {
        List<Object[]> result = newArrayList(randomBytesArray(100), randomBytesArray(1000),
                randomBytesArray(10000), randomBytesArray(2000000));
        return result;
    }

    private static Object[] randomBytesArray(int length) {
        byte[] result = new byte[length];
        (new Random()).nextBytes(result);
        return new Object[]{result};
    }

}
