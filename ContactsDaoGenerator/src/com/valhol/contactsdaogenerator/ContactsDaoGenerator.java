/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.valhol.contactsdaogenerator;

import de.greenrobot.daogenerator.*;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class ContactsDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.valhol.SolstCodeExam.dao");

        addContact(schema);

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }

    private static void addContact(Schema schema) {
        Entity phones = schema.addEntity("Phone");
        phones.addIdProperty();
        phones.addStringProperty("work");
        phones.addStringProperty("home");
        phones.addStringProperty("mobile");
        Property contactId = phones.addLongProperty("contactId").getProperty();

        Entity contact = schema.addEntity("Contact");
        contact.addIdProperty();
        contact.addStringProperty("name");
        contact.addLongProperty("employeeId").unique();
        contact.addStringProperty("company");
        contact.addStringProperty("detailsURL");
        contact.addStringProperty("smallImageURL");
        contact.addStringProperty("birthdate");
        Property phonesId = contact.addLongProperty("phonesId").getProperty();
        Property detailsId = contact.addLongProperty("contactDetailsId").getProperty();

        phones.addToOne(contact, contactId);
        contact.addToOne(phones, phonesId)
                .setName("phone");

        Entity address = schema.addEntity("Address");
        address.addIdProperty();
        address.addStringProperty("street");
        address.addStringProperty("city");
        address.addStringProperty("state");
        address.addStringProperty("country");
        address.addStringProperty("zip");
        address.addStringProperty("latitude");
        address.addStringProperty("longitude");
        Property contactDetailId = address.addLongProperty("contactDetailId").getProperty();

        Entity contactDetails = schema.addEntity("ContactDetails");
        contactDetails.addIdProperty();
        contactDetails.addLongProperty("employeeId").unique();
        contactDetails.addBooleanProperty("favorite");
        contactDetails.addStringProperty("largeImageURL");
        contactDetails.addStringProperty("email");
        contactDetails.addStringProperty("website");
        Property contactAddressId = contactDetails.addLongProperty("addressId").getProperty();

        contactDetails.addToOne(address, contactAddressId);
        address.addToOne(contactDetails, contactDetailId);
        contact.addToOne(contactDetails, detailsId);
    }

}
