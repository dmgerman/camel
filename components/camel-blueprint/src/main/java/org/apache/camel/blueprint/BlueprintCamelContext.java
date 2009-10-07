begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
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
name|ObjectHelper
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

begin_class
DECL|class|BlueprintCamelContext
specifier|public
class|class
name|BlueprintCamelContext
extends|extends
name|DefaultCamelContext
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
name|BlueprintCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|shouldStartContext
specifier|private
name|boolean
name|shouldStartContext
init|=
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
literal|"shouldStartContext"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|maybeStart
argument_list|()
expr_stmt|;
block|}
DECL|method|maybeStart ()
specifier|private
name|void
name|maybeStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getShouldStartContext
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Not starting Apache Camel as property ShouldStartContext is false"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|isStarted
argument_list|()
operator|&&
operator|!
name|isStarting
argument_list|()
condition|)
block|{
comment|// Make sure we will not get into the endless loop of calling star
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Apache Camel as property ShouldStartContext is true"
argument_list|)
expr_stmt|;
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// ignore as Camel is already started
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring maybeStart() as Apache Camel is already started"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|setShouldStartContext (boolean shouldStartContext)
specifier|public
name|void
name|setShouldStartContext
parameter_list|(
name|boolean
name|shouldStartContext
parameter_list|)
block|{
name|this
operator|.
name|shouldStartContext
operator|=
name|shouldStartContext
expr_stmt|;
block|}
DECL|method|getShouldStartContext ()
specifier|public
name|boolean
name|getShouldStartContext
parameter_list|()
block|{
return|return
name|shouldStartContext
return|;
block|}
block|}
end_class

end_unit

