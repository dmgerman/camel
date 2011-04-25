begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
operator|.
name|util
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Represents the options for the client authentication settings of a server socket.  */
end_comment

begin_enum
annotation|@
name|XmlEnum
argument_list|(
name|value
operator|=
name|String
operator|.
name|class
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"clientAuthenticationParameters"
argument_list|)
DECL|enum|ClientAuthenticationDefinition
specifier|public
enum|enum
name|ClientAuthenticationDefinition
block|{
comment|/**      * No client authentication required or requested.      */
DECL|enumConstant|NONE
name|NONE
block|,
comment|/**      * Client authentication requested.      */
DECL|enumConstant|WANT
name|WANT
block|,
comment|/**      * Client authentication required.      */
DECL|enumConstant|REQUIRE
name|REQUIRE
block|; }
end_enum

end_unit

