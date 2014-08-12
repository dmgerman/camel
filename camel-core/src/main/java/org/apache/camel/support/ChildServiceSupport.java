begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * Base class to control lifecycle for a set of child {@link org.apache.camel.Service}s.  */
end_comment

begin_class
DECL|class|ChildServiceSupport
specifier|public
specifier|abstract
class|class
name|ChildServiceSupport
extends|extends
name|ServiceSupport
block|{
DECL|field|childServices
specifier|private
name|Set
argument_list|<
name|Object
argument_list|>
name|childServices
decl_stmt|;
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|start
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|start (boolean startChildren)
specifier|public
name|void
name|start
parameter_list|(
name|boolean
name|startChildren
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
if|if
condition|(
name|starting
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|boolean
name|childrenStarted
init|=
literal|false
decl_stmt|;
name|Exception
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|childServices
operator|!=
literal|null
operator|&&
name|startChildren
condition|)
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
name|childrenStarted
operator|=
literal|true
expr_stmt|;
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stop
argument_list|(
name|childrenStarted
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore exceptions as we want to show the original exception
block|}
throw|throw
name|ex
throw|;
block|}
else|else
block|{
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopped
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|stop (boolean childrenStarted)
specifier|private
name|void
name|stop
parameter_list|(
name|boolean
name|childrenStarted
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|stopping
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
try|try
block|{
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|childrenStarted
condition|)
block|{
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|childServices
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|stopped
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stopping
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|starting
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspending
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suspended
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|stopped
operator|.
name|get
argument_list|()
condition|)
block|{
name|stop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure we are stopped first
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|shuttingdown
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
try|try
block|{
try|try
block|{
name|doShutdown
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|childServices
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|childServices
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|// shutdown is also stopped so only set shutdown flags
name|shutdown
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|shuttingdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addChildService (Object childService)
specifier|protected
name|void
name|addChildService
parameter_list|(
name|Object
name|childService
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|childServices
operator|==
literal|null
condition|)
block|{
name|childServices
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
name|childServices
operator|.
name|add
argument_list|(
name|childService
argument_list|)
expr_stmt|;
block|}
DECL|method|removeChildService (Object childService)
specifier|protected
name|boolean
name|removeChildService
parameter_list|(
name|Object
name|childService
parameter_list|)
block|{
return|return
name|childServices
operator|!=
literal|null
operator|&&
name|childServices
operator|.
name|remove
argument_list|(
name|childService
argument_list|)
return|;
block|}
block|}
end_class

end_unit

