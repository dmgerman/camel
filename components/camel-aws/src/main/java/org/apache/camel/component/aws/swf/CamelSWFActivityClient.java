begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
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
name|swf
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DynamicActivitiesClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DynamicActivitiesClientImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|core
operator|.
name|Promise
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|model
operator|.
name|ActivityType
import|;
end_import

begin_class
DECL|class|CamelSWFActivityClient
specifier|public
class|class
name|CamelSWFActivityClient
block|{
DECL|field|dynamicActivitiesClient
specifier|private
specifier|final
name|DynamicActivitiesClient
name|dynamicActivitiesClient
decl_stmt|;
DECL|field|configuration
specifier|private
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|method|CamelSWFActivityClient (SWFConfiguration configuration)
specifier|public
name|CamelSWFActivityClient
parameter_list|(
name|SWFConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|dynamicActivitiesClient
operator|=
name|getDynamicActivitiesClient
argument_list|()
expr_stmt|;
block|}
DECL|method|scheduleActivity (String eventName, String version, Object input)
specifier|public
name|Object
name|scheduleActivity
parameter_list|(
name|String
name|eventName
parameter_list|,
name|String
name|version
parameter_list|,
name|Object
name|input
parameter_list|)
block|{
name|ActivityType
name|activity
init|=
operator|new
name|ActivityType
argument_list|()
decl_stmt|;
name|activity
operator|.
name|setName
argument_list|(
name|eventName
argument_list|)
expr_stmt|;
name|activity
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|Promise
argument_list|<
name|?
argument_list|>
index|[]
name|promises
init|=
name|asPromiseArray
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|Promise
argument_list|<
name|?
argument_list|>
name|promise
init|=
name|dynamicActivitiesClient
operator|.
name|scheduleActivity
argument_list|(
name|activity
argument_list|,
name|promises
argument_list|,
name|configuration
operator|.
name|getActivitySchedulingOptions
argument_list|()
argument_list|,
name|Object
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|promise
return|;
block|}
DECL|method|asPromiseArray (Object input)
specifier|protected
name|Promise
argument_list|<
name|?
argument_list|>
index|[]
name|asPromiseArray
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
name|Promise
argument_list|<
name|?
argument_list|>
index|[]
name|promises
decl_stmt|;
if|if
condition|(
name|input
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|Object
index|[]
name|inputArray
init|=
operator|(
name|Object
index|[]
operator|)
name|input
decl_stmt|;
name|promises
operator|=
operator|new
name|Promise
index|[
name|inputArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|inputArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|promises
index|[
name|i
index|]
operator|=
name|Promise
operator|.
name|asPromise
argument_list|(
name|inputArray
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|promises
operator|=
operator|new
name|Promise
index|[
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|input
operator|instanceof
name|Promise
condition|)
block|{
name|promises
index|[
literal|0
index|]
operator|=
operator|(
name|Promise
argument_list|<
name|?
argument_list|>
operator|)
name|input
expr_stmt|;
block|}
else|else
block|{
name|promises
index|[
literal|0
index|]
operator|=
name|Promise
operator|.
name|asPromise
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|promises
return|;
block|}
DECL|method|getDynamicActivitiesClient ()
name|DynamicActivitiesClient
name|getDynamicActivitiesClient
parameter_list|()
block|{
return|return
operator|new
name|DynamicActivitiesClientImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

