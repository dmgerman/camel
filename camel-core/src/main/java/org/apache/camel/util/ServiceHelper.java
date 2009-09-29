begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A collection of helper methods for working with {@link Service} objects  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ServiceHelper
specifier|public
specifier|final
class|class
name|ServiceHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ServiceHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ServiceHelper ()
specifier|private
name|ServiceHelper
parameter_list|()
block|{     }
DECL|method|startService (Object value)
specifier|public
specifier|static
name|void
name|startService
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|startServices
argument_list|(
operator|(
name|Collection
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Starts all of the given services      */
DECL|method|startServices (Object... services)
specifier|public
specifier|static
name|void
name|startServices
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
name|startService
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Starts all of the given services      */
DECL|method|startServices (Collection services)
specifier|public
specifier|static
name|void
name|startServices
parameter_list|(
name|Collection
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stops all of the given services, throwing the first exception caught      */
DECL|method|stopServices (Object... services)
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|(
name|Object
modifier|...
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|List
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|services
argument_list|)
decl_stmt|;
name|stopServices
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
DECL|method|stopService (Object value)
specifier|public
specifier|static
name|void
name|stopService
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
name|service
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|stopServices
argument_list|(
operator|(
name|Collection
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Stops all of the given services, throwing the first exception caught      */
DECL|method|stopServices (Collection services)
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|(
name|Collection
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|Exception
name|firstException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|services
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|value
decl_stmt|;
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Stopping service: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught exception shutting down: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|firstException
operator|==
literal|null
condition|)
block|{
name|firstException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
block|}
end_class

end_unit

