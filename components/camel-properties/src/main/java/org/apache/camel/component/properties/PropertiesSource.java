begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * A source for 3rd party properties, such as Eclipse MicroProfile Config, a custom implementation  * that loads properties from a database table, or HashiCorp Vault etc.  */
end_comment

begin_interface
DECL|interface|PropertiesSource
specifier|public
interface|interface
name|PropertiesSource
block|{
comment|/**      * Name of properties source      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Loads the properties from the source      *      * @return the loaded properties      */
DECL|method|loadProperties ()
name|Properties
name|loadProperties
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

