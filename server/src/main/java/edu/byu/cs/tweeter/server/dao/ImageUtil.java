package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import java.io.IOException;


public class ImageUtil
{
    private String BUCKET_NAME = "andy-tweeter-profile-photo-bucket";
    private AmazonS3 s3;
    public ImageUtil()
    {
        s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();
    }

    public void uploadImage(String image, String alias)
    {
        s3.putObject(BUCKET_NAME, alias, image);
    }

    public byte[] getImage(String alias) throws IOException
    {
        S3Object obj = s3.getObject(BUCKET_NAME, alias);
        S3ObjectInputStream stream = obj.getObjectContent();
        return IOUtils.toByteArray(stream);
    }
}
