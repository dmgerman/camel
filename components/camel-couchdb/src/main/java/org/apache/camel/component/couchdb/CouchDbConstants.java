begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_interface
DECL|interface|CouchDbConstants
specifier|public
interface|interface
name|CouchDbConstants
block|{
DECL|field|HEADER_DATABASE
name|String
name|HEADER_DATABASE
init|=
literal|"CouchDbDatabase"
decl_stmt|;
DECL|field|HEADER_SEQ
name|String
name|HEADER_SEQ
init|=
literal|"CouchDbSeq"
decl_stmt|;
DECL|field|HEADER_DOC_ID
name|String
name|HEADER_DOC_ID
init|=
literal|"CouchDbId"
decl_stmt|;
DECL|field|HEADER_DOC_REV
name|String
name|HEADER_DOC_REV
init|=
literal|"CouchDbRev"
decl_stmt|;
DECL|field|HEADER_METHOD
name|String
name|HEADER_METHOD
init|=
literal|"CouchDbMethod"
decl_stmt|;
block|}
end_interface

end_unit

