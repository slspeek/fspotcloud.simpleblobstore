package com.googlecode.simpleblobstore.j2ee;

import com.googlecode.simplejpadao.SimpleDAOGenIdImpl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

public class BlobDaoImpl extends SimpleDAOGenIdImpl<Blob, BlobEntity, Long> implements BlobDao {

    @Override
    public Class<BlobEntity> getEntityType() {
        return BlobEntity.class;
    }
}
