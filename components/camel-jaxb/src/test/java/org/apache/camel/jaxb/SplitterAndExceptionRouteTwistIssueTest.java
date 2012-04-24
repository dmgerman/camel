begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|EndpointInject
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
name|LoggingLevel
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
name|Produce
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|junit4
operator|.
name|CamelTestSupport
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SplitterAndExceptionRouteTwistIssueTest
specifier|public
class|class
name|SplitterAndExceptionRouteTwistIssueTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:error"
argument_list|)
DECL|field|templateError
specifier|protected
name|ProducerTemplate
name|templateError
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:error2"
argument_list|)
DECL|field|templateError2
specifier|protected
name|ProducerTemplate
name|templateError2
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:mockReject"
argument_list|)
DECL|field|mockRejectEndpoint
specifier|protected
name|MockEndpoint
name|mockRejectEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:mock_output"
argument_list|)
DECL|field|mockOutput
specifier|protected
name|MockEndpoint
name|mockOutput
decl_stmt|;
annotation|@
name|Test
DECL|method|testErrorHandlingJaxb ()
specifier|public
name|void
name|testErrorHandlingJaxb
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|correctExample
init|=
literal|"abcdef"
decl_stmt|;
name|String
name|errorExample
init|=
literal|"myerror\u0010"
decl_stmt|;
name|mockRejectEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockOutput
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|templateError
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError
operator|.
name|sendBody
argument_list|(
name|errorExample
argument_list|)
expr_stmt|;
name|templateError
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|mockRejectEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockOutput
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testErrorHandlingPlumber ()
specifier|public
name|void
name|testErrorHandlingPlumber
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|correctExample
init|=
literal|"abcdef"
decl_stmt|;
name|String
name|errorExample
init|=
literal|"myerror\u0010"
decl_stmt|;
name|mockRejectEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockOutput
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|templateError2
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError2
operator|.
name|sendBody
argument_list|(
name|errorExample
argument_list|)
expr_stmt|;
name|templateError2
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError2
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|templateError2
operator|.
name|sendBody
argument_list|(
name|correctExample
argument_list|)
expr_stmt|;
name|mockRejectEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockOutput
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
name|mockRejectEndpoint
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|)
operator|.
name|logExhausted
argument_list|(
literal|true
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|true
argument_list|)
operator|.
name|logRetryStackTrace
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:error"
argument_list|)
operator|.
name|handleFault
argument_list|()
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"UTF-8"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|String
name|text
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Twits
name|twits
init|=
operator|new
name|Twits
argument_list|()
decl_stmt|;
name|Twit
name|twit1
init|=
operator|new
name|Twit
argument_list|()
decl_stmt|;
name|twit1
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|twits
operator|.
name|getTwits
argument_list|()
operator|.
name|add
argument_list|(
name|twit1
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|twits
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//twits/twit"
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
name|mockOutput
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:error2"
argument_list|)
operator|.
name|handleFault
argument_list|()
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"UTF-8"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|String
name|text
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|StringBuilder
name|twits
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|twits
operator|.
name|append
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
argument_list|)
expr_stmt|;
name|twits
operator|.
name|append
argument_list|(
literal|"<twits>"
argument_list|)
expr_stmt|;
name|twits
operator|.
name|append
argument_list|(
literal|"<twit>"
argument_list|)
expr_stmt|;
name|twits
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|twits
operator|.
name|append
argument_list|(
literal|"</twit>"
argument_list|)
expr_stmt|;
name|twits
operator|.
name|append
argument_list|(
literal|"</twits>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|twits
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//twits/twit"
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
name|mockOutput
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"twits"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"twits"
argument_list|)
DECL|class|Twits
class|class
name|Twits
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"twit"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|twits
specifier|protected
name|List
argument_list|<
name|Twit
argument_list|>
name|twits
decl_stmt|;
DECL|method|getTwits ()
specifier|public
name|List
argument_list|<
name|Twit
argument_list|>
name|getTwits
parameter_list|()
block|{
if|if
condition|(
name|twits
operator|==
literal|null
condition|)
block|{
name|twits
operator|=
operator|new
name|ArrayList
argument_list|<
name|Twit
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|twits
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|twits
operator|==
literal|null
operator|||
name|twits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|"["
operator|+
name|twits
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"Twit"
argument_list|,
name|propOrder
operator|=
block|{
literal|"text"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"twit"
argument_list|)
DECL|class|Twit
class|class
name|Twit
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|text
specifier|protected
name|String
name|text
decl_stmt|;
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|setText (String value)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|text
return|;
block|}
block|}
end_class

end_unit

