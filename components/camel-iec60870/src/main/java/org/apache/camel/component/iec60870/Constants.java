begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
package|;
end_package

begin_interface
DECL|interface|Constants
specifier|public
interface|interface
name|Constants
block|{
DECL|field|PARAM_DATA_MODULE_OPTIONS
name|String
name|PARAM_DATA_MODULE_OPTIONS
init|=
literal|"dataModuleOptions"
decl_stmt|;
DECL|field|PARAM_PROTOCOL_OPTIONS
name|String
name|PARAM_PROTOCOL_OPTIONS
init|=
literal|"protocolOptions"
decl_stmt|;
DECL|field|PARAM_CONNECTION_OPTIONS
name|String
name|PARAM_CONNECTION_OPTIONS
init|=
literal|"connectionOptions"
decl_stmt|;
block|}
end_interface

end_unit

