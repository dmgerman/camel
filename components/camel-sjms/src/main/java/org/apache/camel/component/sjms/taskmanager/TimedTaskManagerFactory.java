begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.taskmanager
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|taskmanager
package|;
end_package

begin_comment
comment|/**  * TODO Add Class documentation for TimedTaskManagerFactory  *   * @author sully6768  */
end_comment

begin_class
DECL|class|TimedTaskManagerFactory
specifier|public
specifier|final
class|class
name|TimedTaskManagerFactory
block|{
DECL|class|TimedTaskManagerHolder
specifier|private
specifier|static
class|class
name|TimedTaskManagerHolder
block|{
DECL|field|INSTANCE
specifier|private
specifier|final
specifier|static
name|TimedTaskManager
name|INSTANCE
init|=
operator|new
name|TimedTaskManager
argument_list|()
decl_stmt|;
block|}
DECL|method|getInstance ()
specifier|public
specifier|static
name|TimedTaskManager
name|getInstance
parameter_list|()
block|{
return|return
name|TimedTaskManagerHolder
operator|.
name|INSTANCE
return|;
block|}
block|}
end_class

end_unit

