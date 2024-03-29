begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.executor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|executor
package|;
end_package

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

begin_class
DECL|class|TestRunnable
specifier|public
class|class
name|TestRunnable
implements|implements
name|Runnable
block|{
DECL|field|SLEEP_MILLIS
specifier|public
specifier|static
specifier|final
name|long
name|SLEEP_MILLIS
init|=
literal|1000
decl_stmt|;
DECL|field|id
specifier|final
name|int
name|id
decl_stmt|;
DECL|field|runCount
specifier|final
name|int
name|runCount
decl_stmt|;
DECL|field|log
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|started
name|boolean
name|started
decl_stmt|;
DECL|method|TestRunnable (int id, int runCount)
specifier|public
name|TestRunnable
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|runCount
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|runCount
operator|=
name|runCount
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|String
name|threadName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"{}: Runnable {} starting"
argument_list|,
name|threadName
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|int
name|currentRun
init|=
literal|0
decl_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
try|try
block|{
while|while
condition|(
name|started
operator|&&
operator|++
name|currentRun
operator|<=
name|runCount
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_MILLIS
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"{}: Runnable {} running {} of {} runs"
argument_list|,
name|threadName
argument_list|,
name|id
argument_list|,
name|currentRun
argument_list|,
name|runCount
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"{}: Runnable {} interrupted on run {}"
argument_list|,
name|threadName
argument_list|,
name|id
argument_list|,
name|currentRun
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|log
operator|.
name|info
argument_list|(
literal|"{}: Runnable {} exiting after {} runs"
argument_list|,
name|threadName
argument_list|,
name|id
argument_list|,
name|currentRun
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|started
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|status ()
specifier|public
name|String
name|status
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Runnable %d is %s"
argument_list|,
name|id
argument_list|,
name|started
condition|?
literal|"started"
else|:
literal|"stopped"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

