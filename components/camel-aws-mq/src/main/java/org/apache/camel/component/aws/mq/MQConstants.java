begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.mq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|mq
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS MQ module  */
end_comment

begin_interface
DECL|interface|MQConstants
specifier|public
interface|interface
name|MQConstants
block|{
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"CamelAwsMQOperation"
decl_stmt|;
DECL|field|MAX_RESULTS
name|String
name|MAX_RESULTS
init|=
literal|"CamelAwsMQMaxResults"
decl_stmt|;
DECL|field|BROKER_NAME
name|String
name|BROKER_NAME
init|=
literal|"CamelAwsMQBrokerName"
decl_stmt|;
DECL|field|BROKER_ENGINE
name|String
name|BROKER_ENGINE
init|=
literal|"CamelAwsMQBrokerEngine"
decl_stmt|;
DECL|field|BROKER_ENGINE_VERSION
name|String
name|BROKER_ENGINE_VERSION
init|=
literal|"CamelAwsMQBrokerEngineVersion"
decl_stmt|;
DECL|field|BROKER_ID
name|String
name|BROKER_ID
init|=
literal|"CamelAwsMQBrokerID"
decl_stmt|;
DECL|field|CONFIGURATION_ID
name|String
name|CONFIGURATION_ID
init|=
literal|"CamelAwsMQConfigurationID"
decl_stmt|;
DECL|field|BROKER_DEPLOYMENT_MODE
name|String
name|BROKER_DEPLOYMENT_MODE
init|=
literal|"CamelAwsMQBrokerDeploymentMode"
decl_stmt|;
DECL|field|BROKER_INSTANCE_TYPE
name|String
name|BROKER_INSTANCE_TYPE
init|=
literal|"CamelAwsMQBrokerInstanceType"
decl_stmt|;
DECL|field|BROKER_USERS
name|String
name|BROKER_USERS
init|=
literal|"CamelAwsMQBrokerUsers"
decl_stmt|;
DECL|field|BROKER_PUBLICLY_ACCESSIBLE
name|String
name|BROKER_PUBLICLY_ACCESSIBLE
init|=
literal|"CamelAwsMQBrokerPubliclyAccessible"
decl_stmt|;
block|}
end_interface

end_unit

