begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|server
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
name|builder
operator|.
name|RouteBuilder
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
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
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
name|example
operator|.
name|server
operator|.
name|model
operator|.
name|IncrementRequest
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
name|example
operator|.
name|server
operator|.
name|model
operator|.
name|IncrementResponse
import|;
end_import

begin_class
DECL|class|IncrementRoute
specifier|public
class|class
name|IncrementRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxbDataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
name|IncrementRequest
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"spring-ws:rootqname:{http://camel.apache.org/example/increment}incrementRequest?endpointMapping=#endpointMapping"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|IncrementProcessor
argument_list|()
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
expr_stmt|;
block|}
DECL|class|IncrementProcessor
specifier|private
specifier|static
specifier|final
class|class
name|IncrementProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|IncrementRequest
name|request
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|IncrementRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|IncrementResponse
name|response
init|=
operator|new
name|IncrementResponse
argument_list|()
decl_stmt|;
name|int
name|result
init|=
name|request
operator|.
name|getInput
argument_list|()
operator|+
literal|1
decl_stmt|;
comment|// increment input value
name|response
operator|.
name|setResult
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

