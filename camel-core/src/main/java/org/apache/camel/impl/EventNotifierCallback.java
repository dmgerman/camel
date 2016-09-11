begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|AsyncCallback
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
name|Endpoint
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
name|Exchange
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
name|EventHelper
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
name|StopWatch
import|;
end_import

begin_comment
comment|/**  * Helper class to notify on exchange sending events in async engine  */
end_comment

begin_class
DECL|class|EventNotifierCallback
class|class
name|EventNotifierCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|originalCallback
specifier|private
specifier|final
name|AsyncCallback
name|originalCallback
decl_stmt|;
DECL|field|watch
specifier|private
specifier|final
name|StopWatch
name|watch
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|EventNotifierCallback (AsyncCallback originalCallback, Exchange exchange, Endpoint endpoint)
specifier|public
name|EventNotifierCallback
parameter_list|(
name|AsyncCallback
name|originalCallback
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|originalCallback
operator|=
name|originalCallback
expr_stmt|;
name|this
operator|.
name|watch
operator|=
operator|new
name|StopWatch
argument_list|()
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
name|originalCallback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

