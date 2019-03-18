begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_interface
DECL|interface|ConsulHealthActions
specifier|public
interface|interface
name|ConsulHealthActions
block|{
DECL|field|CHECKS
name|String
name|CHECKS
init|=
literal|"CHECKS"
decl_stmt|;
DECL|field|NODE_CHECKS
name|String
name|NODE_CHECKS
init|=
literal|"NODE_CHECKS"
decl_stmt|;
DECL|field|SERVICE_CHECKS
name|String
name|SERVICE_CHECKS
init|=
literal|"SERVICE_CHECKS"
decl_stmt|;
DECL|field|SERVICE_INSTANCES
name|String
name|SERVICE_INSTANCES
init|=
literal|"SERVICE_INSTANCES"
decl_stmt|;
block|}
end_interface

end_unit

