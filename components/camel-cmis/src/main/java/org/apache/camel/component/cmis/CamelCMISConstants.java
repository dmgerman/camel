begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
package|;
end_package

begin_interface
DECL|interface|CamelCMISConstants
specifier|public
interface|interface
name|CamelCMISConstants
block|{
DECL|field|CMIS_DOCUMENT
name|String
name|CMIS_DOCUMENT
init|=
literal|"cmis:document"
decl_stmt|;
DECL|field|CMIS_FOLDER
name|String
name|CMIS_FOLDER
init|=
literal|"cmis:folder"
decl_stmt|;
DECL|field|CMIS_FOLDER_PATH
name|String
name|CMIS_FOLDER_PATH
init|=
literal|"CamelCMISFolderPath"
decl_stmt|;
DECL|field|CMIS_MIME_TYPE
name|String
name|CMIS_MIME_TYPE
init|=
literal|"CamelCMISMimeType"
decl_stmt|;
DECL|field|CAMEL_CMIS_RESULT_COUNT
name|String
name|CAMEL_CMIS_RESULT_COUNT
init|=
literal|"CamelCMISResultCount"
decl_stmt|;
DECL|field|CAMEL_CMIS_RETRIEVE_CONTENT
name|String
name|CAMEL_CMIS_RETRIEVE_CONTENT
init|=
literal|"CamelCMISRetrieveContent"
decl_stmt|;
DECL|field|CAMEL_CMIS_READ_SIZE
name|String
name|CAMEL_CMIS_READ_SIZE
init|=
literal|"CamelCMISReadSize"
decl_stmt|;
DECL|field|CAMEL_CMIS_CONTENT_STREAM
name|String
name|CAMEL_CMIS_CONTENT_STREAM
init|=
literal|"CamelCMISContent"
decl_stmt|;
block|}
end_interface

end_unit

