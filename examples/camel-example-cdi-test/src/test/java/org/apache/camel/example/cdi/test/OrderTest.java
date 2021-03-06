begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|ProducerTemplate
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
name|cdi
operator|.
name|Uri
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
name|test
operator|.
name|cdi
operator|.
name|CamelCdiRunner
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
name|test
operator|.
name|cdi
operator|.
name|Order
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ClassRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|Verifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|contains
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelCdiRunner
operator|.
name|class
argument_list|)
DECL|class|OrderTest
specifier|public
class|class
name|OrderTest
block|{
annotation|@
name|ClassRule
DECL|field|verifier
specifier|public
specifier|static
name|MessageVerifier
name|verifier
init|=
operator|new
name|MessageVerifier
argument_list|()
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"direct:in"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Test
annotation|@
name|Order
argument_list|(
literal|2
argument_list|)
DECL|method|sendMessageTwo ()
specifier|public
name|void
name|sendMessageTwo
parameter_list|()
block|{
name|producer
operator|.
name|sendBody
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Order
argument_list|(
literal|3
argument_list|)
DECL|method|sendMessageThree ()
specifier|public
name|void
name|sendMessageThree
parameter_list|()
block|{
name|producer
operator|.
name|sendBody
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Order
argument_list|(
literal|1
argument_list|)
DECL|method|sendMessageOne ()
specifier|public
name|void
name|sendMessageOne
parameter_list|()
block|{
name|producer
operator|.
name|sendBody
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
block|}
DECL|class|TestRoute
specifier|static
class|class
name|TestRoute
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
block|{
name|from
argument_list|(
literal|"direct:out"
argument_list|)
operator|.
name|process
argument_list|(
name|verifier
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MessageVerifier
specifier|static
class|class
name|MessageVerifier
extends|extends
name|Verifier
implements|implements
name|Processor
block|{
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|verify ()
specifier|protected
name|void
name|verify
parameter_list|()
block|{
name|assertThat
argument_list|(
literal|"Messages sequence is incorrect!"
argument_list|,
name|messages
argument_list|,
name|contains
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|,
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|{
name|messages
operator|.
name|add
argument_list|(
name|exchange
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
block|}
block|}
block|}
end_class

end_unit

