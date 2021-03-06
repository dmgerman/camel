begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|LinkedList
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
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A basic reactive engine that uses a worker pool to process tasks.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|ReactiveHelper
specifier|public
specifier|final
class|class
name|ReactiveHelper
block|{
DECL|field|WORKERS
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|Worker
argument_list|>
name|WORKERS
init|=
name|ThreadLocal
operator|.
name|withInitial
argument_list|(
name|Worker
operator|::
operator|new
argument_list|)
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ReactiveHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ReactiveHelper ()
specifier|private
name|ReactiveHelper
parameter_list|()
block|{     }
DECL|method|scheduleMain (Runnable runnable)
specifier|public
specifier|static
name|void
name|scheduleMain
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|runnable
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|scheduleSync (Runnable runnable)
specifier|public
specifier|static
name|void
name|scheduleSync
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|runnable
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|scheduleMain (Runnable runnable, String description)
specifier|public
specifier|static
name|void
name|scheduleMain
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|schedule (Runnable runnable)
specifier|public
specifier|static
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|runnable
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|schedule (Runnable runnable, String description)
specifier|public
specifier|static
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated not in use      */
annotation|@
name|Deprecated
DECL|method|scheduleLast (Runnable runnable, String description)
specifier|public
specifier|static
name|void
name|scheduleLast
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|scheduleSync (Runnable runnable, String description)
specifier|public
specifier|static
name|void
name|scheduleSync
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|schedule
argument_list|(
name|describe
argument_list|(
name|runnable
argument_list|,
name|description
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|executeFromQueue ()
specifier|public
specifier|static
name|boolean
name|executeFromQueue
parameter_list|()
block|{
return|return
name|WORKERS
operator|.
name|get
argument_list|()
operator|.
name|executeFromQueue
argument_list|()
return|;
block|}
DECL|method|callback (AsyncCallback callback)
specifier|public
specifier|static
name|void
name|callback
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|schedule
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Callback["
operator|+
name|callback
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|describe (Runnable runnable, String description)
specifier|private
specifier|static
name|Runnable
name|describe
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
return|;
block|}
DECL|class|Worker
specifier|private
specifier|static
class|class
name|Worker
block|{
DECL|field|queue
specifier|private
specifier|volatile
name|LinkedList
argument_list|<
name|Runnable
argument_list|>
name|queue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|back
specifier|private
specifier|volatile
name|LinkedList
argument_list|<
name|LinkedList
argument_list|<
name|Runnable
argument_list|>
argument_list|>
name|back
decl_stmt|;
DECL|field|running
specifier|private
specifier|volatile
name|boolean
name|running
decl_stmt|;
DECL|method|schedule (Runnable runnable, boolean first, boolean main, boolean sync)
specifier|public
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|boolean
name|first
parameter_list|,
name|boolean
name|main
parameter_list|,
name|boolean
name|sync
parameter_list|)
block|{
if|if
condition|(
name|main
condition|)
block|{
if|if
condition|(
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|back
operator|==
literal|null
condition|)
block|{
name|back
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|back
operator|.
name|push
argument_list|(
name|queue
argument_list|)
expr_stmt|;
name|queue
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|first
condition|)
block|{
name|queue
operator|.
name|addFirst
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queue
operator|.
name|addLast
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|running
operator|||
name|sync
condition|)
block|{
name|running
operator|=
literal|true
expr_stmt|;
comment|//                Thread thread = Thread.currentThread();
comment|//                String name = thread.getName();
try|try
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
specifier|final
name|Runnable
name|polled
init|=
name|queue
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|polled
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|back
operator|!=
literal|null
operator|&&
operator|!
name|back
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|queue
operator|=
name|back
operator|.
name|poll
argument_list|()
expr_stmt|;
continue|continue;
block|}
else|else
block|{
break|break;
block|}
block|}
try|try
block|{
comment|//                            thread.setName(name + " - " + polled.toString());
name|polled
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error executing reactive work due to "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|//                    thread.setName(name);
name|running
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Queuing reactive work: {}"
argument_list|,
name|runnable
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|executeFromQueue ()
specifier|public
name|boolean
name|executeFromQueue
parameter_list|()
block|{
specifier|final
name|Runnable
name|polled
init|=
name|queue
operator|!=
literal|null
condition|?
name|queue
operator|.
name|poll
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|polled
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Thread
name|thread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|thread
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
name|thread
operator|.
name|setName
argument_list|(
name|name
operator|+
literal|" - "
operator|+
name|polled
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|polled
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// should not happen
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error executing reactive work due to "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|thread
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

