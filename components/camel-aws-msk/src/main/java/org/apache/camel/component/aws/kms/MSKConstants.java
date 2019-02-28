begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kms
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
name|kms
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS MSK module  */
end_comment

begin_interface
DECL|interface|MSKConstants
specifier|public
interface|interface
name|MSKConstants
block|{
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"CamelAwsMSKOperation"
decl_stmt|;
DECL|field|CLUSTERS_FILTER
name|String
name|CLUSTERS_FILTER
init|=
literal|"CamelAwsMSKClusterFilter"
decl_stmt|;
block|}
end_interface

end_unit

