begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_comment
comment|/**  * Cometd specific converters.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CometdConverters
specifier|public
specifier|final
class|class
name|CometdConverters
block|{
DECL|method|CometdConverters ()
specifier|private
name|CometdConverters
parameter_list|()
block|{
comment|//Utility Class
block|}
comment|/**      * Converts the given JavaMail message to a String body.      * Can return null.      */
comment|/*         @Converter     public static String toString(Message message) throws MessagingException, IOException {         Object content = message.getContent();         if (content instanceof MimeMultipart) {             MimeMultipart multipart = (MimeMultipart) content;             if (multipart.getCount()> 0) {                 BodyPart part = multipart.getBodyPart(0);                 content = part.getContent();             }         }         if (content != null) {             return content.toString();         }         return null;     } */
block|}
end_class

end_unit

