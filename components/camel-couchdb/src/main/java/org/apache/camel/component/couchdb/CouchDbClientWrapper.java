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

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|Changes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|CouchDbClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|CouchDbContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  * Necessary to allow mockito to mock this client.  * Once LightCouch library adds an interface for the client, this class can be removed.  */
end_comment

begin_class
DECL|class|CouchDbClientWrapper
specifier|public
class|class
name|CouchDbClientWrapper
block|{
DECL|field|client
specifier|private
specifier|final
name|CouchDbClient
name|client
decl_stmt|;
DECL|method|CouchDbClientWrapper (CouchDbClient client)
specifier|public
name|CouchDbClientWrapper
parameter_list|(
name|CouchDbClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|update (Object doc)
specifier|public
name|Response
name|update
parameter_list|(
name|Object
name|doc
parameter_list|)
block|{
return|return
name|client
operator|.
name|update
argument_list|(
name|doc
argument_list|)
return|;
block|}
DECL|method|save (Object doc)
specifier|public
name|Response
name|save
parameter_list|(
name|Object
name|doc
parameter_list|)
block|{
return|return
name|client
operator|.
name|save
argument_list|(
name|doc
argument_list|)
return|;
block|}
DECL|method|changes ()
specifier|public
name|Changes
name|changes
parameter_list|()
block|{
return|return
name|client
operator|.
name|changes
argument_list|()
return|;
block|}
DECL|method|context ()
specifier|public
name|CouchDbContext
name|context
parameter_list|()
block|{
return|return
name|client
operator|.
name|context
argument_list|()
return|;
block|}
block|}
end_class

end_unit

