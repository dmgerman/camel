begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz2
package|;
end_package

begin_comment
comment|/**  * Provide some constants used in this component package.  */
end_comment

begin_class
DECL|class|QuartzConstants
specifier|public
specifier|final
class|class
name|QuartzConstants
block|{
DECL|field|QUARTZ_CAMEL_JOBS_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|QUARTZ_CAMEL_JOBS_COUNT
init|=
literal|"CamelQuartzJobsCount"
decl_stmt|;
DECL|field|QUARTZ_ENDPOINT_URI
specifier|public
specifier|static
specifier|final
name|String
name|QUARTZ_ENDPOINT_URI
init|=
literal|"CamelQuartzEndpoint"
decl_stmt|;
comment|// Note: using the CamelContext management name to ensure its unique in the JVM
DECL|field|QUARTZ_CAMEL_CONTEXT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|QUARTZ_CAMEL_CONTEXT_NAME
init|=
literal|"CamelQuartzCamelContextName"
decl_stmt|;
DECL|field|QUARTZ_CAMEL_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|QUARTZ_CAMEL_CONTEXT
init|=
literal|"CamelQuartzCamelContext"
decl_stmt|;
DECL|method|QuartzConstants ()
specifier|private
name|QuartzConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

