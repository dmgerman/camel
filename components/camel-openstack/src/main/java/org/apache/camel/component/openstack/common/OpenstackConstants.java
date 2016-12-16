begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|common
package|;
end_package

begin_comment
comment|/**  * General camel-openstack component constants.  * The main purpose for this class is to avoid duplication general constants in each submodule.  */
end_comment

begin_class
DECL|class|OpenstackConstants
specifier|public
class|class
name|OpenstackConstants
block|{
DECL|field|OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION
init|=
literal|"operation"
decl_stmt|;
DECL|field|ID
specifier|public
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"ID"
decl_stmt|;
DECL|field|NAME
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|DESCRIPTION
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
DECL|field|PROPERTIES
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTIES
init|=
literal|"properties"
decl_stmt|;
DECL|field|CREATE
specifier|public
specifier|static
specifier|final
name|String
name|CREATE
init|=
literal|"create"
decl_stmt|;
DECL|field|UPDATE
specifier|public
specifier|static
specifier|final
name|String
name|UPDATE
init|=
literal|"update"
decl_stmt|;
DECL|field|GET_ALL
specifier|public
specifier|static
specifier|final
name|String
name|GET_ALL
init|=
literal|"getAll"
decl_stmt|;
DECL|field|GET
specifier|public
specifier|static
specifier|final
name|String
name|GET
init|=
literal|"get"
decl_stmt|;
DECL|field|DELETE
specifier|public
specifier|static
specifier|final
name|String
name|DELETE
init|=
literal|"delete"
decl_stmt|;
DECL|method|OpenstackConstants ()
specifier|protected
name|OpenstackConstants
parameter_list|()
block|{ }
block|}
end_class

end_unit

