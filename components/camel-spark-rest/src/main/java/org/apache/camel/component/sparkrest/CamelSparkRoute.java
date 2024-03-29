begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|ExchangePattern
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
name|Message
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
name|Processor
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
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Route
import|;
end_import

begin_class
DECL|class|CamelSparkRoute
specifier|public
class|class
name|CamelSparkRoute
implements|implements
name|Route
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|SparkEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|CamelSparkRoute (SparkEndpoint endpoint, Processor processor)
specifier|public
name|CamelSparkRoute
parameter_list|(
name|SparkEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handle (Request request, Response response)
specifier|public
name|Object
name|handle
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
name|in
init|=
name|endpoint
operator|.
name|getSparkBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|,
name|endpoint
operator|.
name|getSparkConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|Message
name|msg
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|endpoint
operator|.
name|getSparkBinding
argument_list|()
operator|.
name|toSparkResponse
argument_list|(
name|msg
argument_list|,
name|response
argument_list|,
name|endpoint
operator|.
name|getSparkConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

