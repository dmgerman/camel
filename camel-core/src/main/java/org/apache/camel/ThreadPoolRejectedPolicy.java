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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolExecutor
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
comment|/**  * Represent the kinds of options for rejection handlers for thread pools.  *<p/>  * These options are used for fine grained thread pool settings, where you  * want to control which handler to use when a thread pool cannot execute  * a new task.  *<p/>  * Camel will by default use<tt>CallerRuns</tt>.  *  * @version $Revision$  */
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
DECL|enum|ThreadPoolRejectedPolicy
specifier|public
enum|enum
name|ThreadPoolRejectedPolicy
block|{
DECL|enumConstant|Abort
DECL|enumConstant|CallerRuns
DECL|enumConstant|DiscardOldest
DECL|enumConstant|Discard
name|Abort
block|,
name|CallerRuns
block|,
name|DiscardOldest
block|,
name|Discard
block|;
DECL|method|asRejectedExecutionHandler ()
specifier|public
name|RejectedExecutionHandler
name|asRejectedExecutionHandler
parameter_list|()
block|{
if|if
condition|(
name|this
operator|==
name|Abort
condition|)
block|{
return|return
operator|new
name|ThreadPoolExecutor
operator|.
name|AbortPolicy
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|==
name|CallerRuns
condition|)
block|{
return|return
operator|new
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|==
name|DiscardOldest
condition|)
block|{
return|return
operator|new
name|ThreadPoolExecutor
operator|.
name|DiscardOldestPolicy
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|==
name|Discard
condition|)
block|{
return|return
operator|new
name|ThreadPoolExecutor
operator|.
name|DiscardPolicy
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown ThreadPoolRejectedPolicy: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
end_enum

end_unit

