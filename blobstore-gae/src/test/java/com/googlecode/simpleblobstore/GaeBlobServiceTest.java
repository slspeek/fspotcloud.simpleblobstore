package com.googlecode.simpleblobstore;


import com.google.guiceberry.junit4.GuiceBerryRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GaeBlobServiceTest {

    public static final int DATA_LEN = 2000200; //2M
    @Rule
    public GuiceBerryRule guiceBerry = new GuiceBerryRule(GaeGuiceBerryEnv.class);


    @Inject
    BlobService service;

    @Test
    public void testDelete() throws Exception {
        byte[] data = new byte[DATA_LEN];

        BlobKey key = service.save("img/jpeg", data);
        data = null;

        data = service.fetchData(key);
        assertNotNull(data);
        assertEquals(DATA_LEN, data.length);
        service.delete(key);
        data = service.fetchData(key);
    }

    @Test
    public void testSave() throws Exception {
        byte[] data = new byte[DATA_LEN];

        BlobKey key = service.save("img/jpeg", data);
        data = null;

        data = service.fetchData(key);
        assertNotNull(data);
        assertEquals(DATA_LEN, data.length);
    }

    public void testGetInfo() throws Exception {
        byte[] data = new byte[DATA_LEN];
        BlobKey key = service.save("img/jpeg", data);
        assertEquals(DATA_LEN, service.getInfo(key).getLength());
    }

    public void testFetchData() throws Exception {

    }
}
