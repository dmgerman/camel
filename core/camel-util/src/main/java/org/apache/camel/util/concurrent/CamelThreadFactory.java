begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
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
name|ThreadFactory
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
comment|/**  * Thread factory which creates threads supporting a naming pattern.  */
end_comment

begin_class
DECL|class|CamelThreadFactory
specifier|public
specifier|final
class|class
name|CamelThreadFactory
implements|implements
name|ThreadFactory
block|{
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
name|CamelThreadFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|daemon
specifier|private
specifier|final
name|boolean
name|daemon
decl_stmt|;
DECL|method|CamelThreadFactory (String pattern, String name, boolean daemon)
specifier|public
name|CamelThreadFactory
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
block|}
DECL|method|newThread (Runnable runnable)
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|String
name|threadName
init|=
name|ThreadHelper
operator|.
name|resolveThreadName
argument_list|(
name|pattern
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|runnable
argument_list|,
name|threadName
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created thread[{}] -> {}"
argument_list|,
name|threadName
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CamelThreadFactory["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

