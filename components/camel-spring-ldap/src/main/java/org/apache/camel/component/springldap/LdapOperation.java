begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.springldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|springldap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|core
operator|.
name|LdapOperations
import|;
end_import

begin_comment
comment|/**  * The list of supported LDAP operations. Currently supported operations are  * search, bind, and unbind, authenticate and modify_attributes. The  * function_driven operation expects a request {@link Object} along with an  * instance of {@link BiFunction} that can be used to invoke any  * method on the {@link LdapOperations} instance  */
end_comment

begin_enum
DECL|enum|LdapOperation
specifier|public
enum|enum
name|LdapOperation
block|{
DECL|enumConstant|SEARCH
DECL|enumConstant|BIND
DECL|enumConstant|UNBIND
DECL|enumConstant|AUTHENTICATE
DECL|enumConstant|MODIFY_ATTRIBUTES
DECL|enumConstant|FUNCTION_DRIVEN
name|SEARCH
block|,
name|BIND
block|,
name|UNBIND
block|,
name|AUTHENTICATE
block|,
name|MODIFY_ATTRIBUTES
block|,
name|FUNCTION_DRIVEN
block|}
end_enum

end_unit

