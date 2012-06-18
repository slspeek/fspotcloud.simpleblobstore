package com.googlecode.simpleblobstore;

import com.googlecode.simplejpadao.SimpleDAOGenIdImpl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

public class BlobDaoImpl extends SimpleDAOGenIdImpl<Blob, BlobEntity, Long> implements BlobDao {
    @Inject
    public BlobDaoImpl(Provider<EntityManager> emProvider) {
        super(BlobEntity.class, emProvider);
    }
}
