/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.jaxrs.client;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

public final class Client {

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        File wibble = new File(args[0]);
        File truststore = new File(args[1]);

        Protocol authhttps = new Protocol("https",
                                          new AuthSSLProtocolSocketFactory(wibble.toURL(), "password",
                                                                           truststore.toURL(), "password"),
                                          9000);
        Protocol.registerProtocol("https", authhttps);

        // Sent HTTP GET request to query customer info
        System.out.println("Sent HTTPS GET request to query customer info");
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod("https://localhost:9000/customerservice/customers/123");
        try {
            httpclient.executeMethod(httpget);
            System.out.println(httpget.getResponseBodyAsString());
        } finally {
            httpget.releaseConnection();
        }

        // Sent HTTP GET request to query sub resource product info
        System.out.println("\n");
        System.out.println("Sent HTTPS GET request to query sub resource product info");

        httpget = new GetMethod("https://localhost:9000/customerservice/orders/223/products/323");
        try {
            httpclient.executeMethod(httpget);
            System.out.println(httpget.getResponseBodyAsString());
        } finally {
            httpget.releaseConnection();
        }

        // Sent HTTP PUT request to update customer info
        System.out.println("\n");
        System.out.println("Sent HTTPS PUT request to update customer info");
        Client client = new Client();
        String inputFile = client.getClass().getResource("update_customer.txt").getFile();
        File input = new File(inputFile);
        PutMethod put = new PutMethod("https://localhost:9000/customerservice/customers");
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        put.setRequestEntity(entity);

        try {
            int result = httpclient.executeMethod(put);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(put.getResponseBodyAsString());
        } finally {
            put.releaseConnection();
        }

        // Sent HTTP POST request to add customer
        System.out.println("Sent HTTPS POST request to add customer");
        inputFile = client.getClass().getResource("add_customer.txt").getFile();
        input = new File(inputFile);
        PostMethod post = new PostMethod("https://localhost:9000/customerservice/customers");
        entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        post.setRequestEntity(entity);

        try {
            int result = httpclient.executeMethod(post);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are
            // done
            post.releaseConnection();
        }

        // Sent HTTP GET request to query customer info, expect JSON.
        System.out.println("\n");
        System.out.println("Sent HTTPS GET request to query customer info in JSON");

        httpget = new GetMethod("https://localhost:9000/customerservice/customersjson/123");
        try {
            httpclient.executeMethod(httpget);
            System.out.println(httpget.getResponseBodyAsString());
        } finally {
            httpget.releaseConnection();
        }

        System.out.println("Client Invoking is succeeded!");
        System.exit(0);
    }
}
