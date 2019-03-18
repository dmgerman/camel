begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_comment
comment|/**  * Resolver to determine Content-Type for file attachments.  *<p/>  * Strategy introduced to work around mail providers having problems with this such as geronimo mail jars.  *<p/>  * Note using SUN mail jar have no problem with resolving Content-Type based on file attachments. This resolver  * is thus only needed to work around mail providers having bugs or when you a new mime type is unknown by the  * mail provider allowing you to determine it.  */
end_comment

begin_interface
DECL|interface|ContentTypeResolver
specifier|public
interface|interface
name|ContentTypeResolver
block|{
comment|/**      * Resolves the mime content-type based on the attachment file name.      *<p/>      * Return<tt>null</tt> if you cannot resolve a content type or want to rely on the mail provider      * to resolve it for you.      *<p/>      * The returned value should only be the mime part of the ContentType header, for example:      *<tt>image/jpeg</tt> should be returned. Camel will add the remaining<tt>; name=FILENAME</tt>.      *      * @param fileName  the attachment file name      * @return the Content-Type or<tt>null</tt> to rely on the mail provider      */
DECL|method|resolveContentType (String fileName)
name|String
name|resolveContentType
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

