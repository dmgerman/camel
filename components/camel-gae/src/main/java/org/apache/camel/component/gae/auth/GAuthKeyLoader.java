begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContextAware
import|;
end_import

begin_comment
comment|/**  * Interface used by {@link GAuthComponent} for loading private keys. The private  * key is needed for RSA-SHA1 signatures.  */
end_comment

begin_interface
DECL|interface|GAuthKeyLoader
specifier|public
interface|interface
name|GAuthKeyLoader
extends|extends
name|CamelContextAware
block|{
comment|/**      * Loads a private key.      *       * @return the loaded private key.      * @throws Exception if key loading failed.      */
DECL|method|loadPrivateKey ()
name|PrivateKey
name|loadPrivateKey
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

