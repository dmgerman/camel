begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.swift
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
name|swift
package|;
end_package

begin_import
import|import
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
operator|.
name|OpenstackConstants
import|;
end_import

begin_class
DECL|class|SwiftConstants
specifier|public
specifier|final
class|class
name|SwiftConstants
extends|extends
name|OpenstackConstants
block|{
DECL|field|SWIFT_SUBSYSTEM_OBJECTS
specifier|public
specifier|static
specifier|final
name|String
name|SWIFT_SUBSYSTEM_OBJECTS
init|=
literal|"objects"
decl_stmt|;
DECL|field|SWIFT_SUBSYSTEM_CONTAINERS
specifier|public
specifier|static
specifier|final
name|String
name|SWIFT_SUBSYSTEM_CONTAINERS
init|=
literal|"containers"
decl_stmt|;
DECL|field|CONTAINER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NAME
init|=
literal|"containerName"
decl_stmt|;
DECL|field|OBJECT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_NAME
init|=
literal|"objectName"
decl_stmt|;
DECL|field|LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|LIMIT
init|=
literal|"limit"
decl_stmt|;
DECL|field|MARKER
specifier|public
specifier|static
specifier|final
name|String
name|MARKER
init|=
literal|"marker"
decl_stmt|;
DECL|field|END_MARKER
specifier|public
specifier|static
specifier|final
name|String
name|END_MARKER
init|=
literal|"end_marker"
decl_stmt|;
DECL|field|DELIMITER
specifier|public
specifier|static
specifier|final
name|String
name|DELIMITER
init|=
literal|"delimiter"
decl_stmt|;
DECL|field|PATH
specifier|public
specifier|static
specifier|final
name|String
name|PATH
init|=
literal|"path"
decl_stmt|;
DECL|field|GET_METADATA
specifier|public
specifier|static
specifier|final
name|String
name|GET_METADATA
init|=
literal|"getMetadata"
decl_stmt|;
DECL|field|CREATE_UPDATE_METADATA
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_UPDATE_METADATA
init|=
literal|"createUpdateMetadata"
decl_stmt|;
DECL|field|DELETE_METADATA
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_METADATA
init|=
literal|"deleteMetadata"
decl_stmt|;
block|}
end_class

end_unit

