begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
import|;
end_import

begin_interface
DECL|interface|EtcdEndpoint
interface|interface
name|EtcdEndpoint
block|{
DECL|method|getConfiguration ()
name|EtcdConfiguration
name|getConfiguration
parameter_list|()
function_decl|;
DECL|method|getNamespace ()
name|EtcdNamespace
name|getNamespace
parameter_list|()
function_decl|;
DECL|method|getPath ()
name|String
name|getPath
parameter_list|()
function_decl|;
DECL|method|createClient ()
name|EtcdClient
name|createClient
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

