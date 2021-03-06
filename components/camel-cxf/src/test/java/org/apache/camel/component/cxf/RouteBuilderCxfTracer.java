begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPerson
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPersonResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageContentsList
import|;
end_import

begin_class
DECL|class|RouteBuilderCxfTracer
specifier|public
class|class
name|RouteBuilderCxfTracer
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
name|from
argument_list|(
literal|"cxf:http://localhost:9000/PersonService/"
operator|+
literal|"?serviceClass=org.apache.camel.non_wrapper.Person"
operator|+
literal|"&serviceName={http://camel.apache.org/non-wrapper}PersonService"
operator|+
literal|"&portName={http://camel.apache.org/non-wrapper}soap"
operator|+
literal|"&dataFormat=POJO"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|BeforeProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:something"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AfterProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:something"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|DoSomethingProcessor
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|DoNothingProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|DoSomethingProcessor
specifier|private
specifier|static
class|class
name|DoSomethingProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|+
literal|" world!"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|DoNothingProcessor
specifier|private
specifier|static
class|class
name|DoNothingProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|BeforeProcessor
specifier|private
specifier|static
class|class
name|BeforeProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange e)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageContentsList
name|mclIn
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MessageContentsList
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|(
operator|(
name|GetPerson
operator|)
name|mclIn
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getPersonId
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AfterProcessor
specifier|private
specifier|static
class|class
name|AfterProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange e)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|GetPersonResponse
name|gpr
init|=
operator|new
name|GetPersonResponse
argument_list|()
decl_stmt|;
name|gpr
operator|.
name|setName
argument_list|(
literal|"Bill"
argument_list|)
expr_stmt|;
name|gpr
operator|.
name|setPersonId
argument_list|(
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|gpr
operator|.
name|setSsn
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|MessageContentsList
name|mclOut
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|mclOut
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|gpr
argument_list|)
expr_stmt|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|mclOut
argument_list|,
name|MessageContentsList
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

