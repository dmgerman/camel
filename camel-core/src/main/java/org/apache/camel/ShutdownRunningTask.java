begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Represents the kind of options for what to do with the current task when shutting down.  *<p/>  * By default the current task is allowed to complete. However some consumers such as  * {@link BatchConsumer} has pending tasks which you can configure to  * let it complete all those tasks as well.  *<ul>  *<li>CompleteCurrentTaskOnly - Is the<b>default</b> behavior where a route consumer will be shutdown as fast as  *                                 possible. Allowing it to complete its current task, but not to pickup pending  *                                 tasks (if any).</li>  *<li>CompleteAllTasks - Allows a route consumer to do<i>do its business</i> which means that it will continue  *                          to complete any pending tasks (if any).</li>  *</ul>  *<b>Notice:</b> Most consumers only have a single tasks, but {@link org.apache.camel.BatchConsumer} can have  * many tasks and thus this option mostly apply to these kind of consumers.  */
end_comment

begin_enum
annotation|@
name|XmlType
annotation|@
name|XmlEnum
argument_list|(
name|String
operator|.
name|class
argument_list|)
DECL|enum|ShutdownRunningTask
specifier|public
enum|enum
name|ShutdownRunningTask
block|{
DECL|enumConstant|CompleteCurrentTaskOnly
DECL|enumConstant|CompleteAllTasks
name|CompleteCurrentTaskOnly
block|,
name|CompleteAllTasks
block|}
end_enum

end_unit

