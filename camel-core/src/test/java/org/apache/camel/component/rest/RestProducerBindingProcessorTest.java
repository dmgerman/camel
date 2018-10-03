begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|AsyncProcessor
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
name|CamelContext
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
name|spi
operator|.
name|DataFormat
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
name|support
operator|.
name|DefaultExchange
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
name|support
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|mockito
operator|.
name|ArgumentCaptor
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
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|same
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|RestProducerBindingProcessorTest
specifier|public
class|class
name|RestProducerBindingProcessorTest
block|{
DECL|class|RequestPojo
specifier|public
specifier|static
class|class
name|RequestPojo
block|{     }
DECL|class|ResponsePojo
specifier|public
specifier|static
class|class
name|ResponsePojo
block|{     }
DECL|field|callback
specifier|final
name|AsyncCallback
name|callback
init|=
name|mock
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|jsonDataFormat
specifier|final
name|DataFormat
name|jsonDataFormat
init|=
name|mock
argument_list|(
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|outJsonDataFormat
specifier|final
name|DataFormat
name|outJsonDataFormat
init|=
name|mock
argument_list|(
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|outXmlDataFormat
specifier|final
name|DataFormat
name|outXmlDataFormat
init|=
name|mock
argument_list|(
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|processor
specifier|final
name|AsyncProcessor
name|processor
init|=
name|mock
argument_list|(
name|AsyncProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xmlDataFormat
specifier|final
name|DataFormat
name|xmlDataFormat
init|=
name|mock
argument_list|(
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldMarshalAndUnmarshalJson ()
specifier|public
name|void
name|shouldMarshalAndUnmarshalJson
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|outType
init|=
name|ResponsePojo
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|RestProducerBindingProcessor
name|bindingProcessor
init|=
operator|new
name|RestProducerBindingProcessor
argument_list|(
name|processor
argument_list|,
name|context
argument_list|,
name|jsonDataFormat
argument_list|,
name|xmlDataFormat
argument_list|,
name|outJsonDataFormat
argument_list|,
name|outXmlDataFormat
argument_list|,
literal|"json"
argument_list|,
literal|true
argument_list|,
name|outType
argument_list|)
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|input
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|RequestPojo
name|request
init|=
operator|new
name|RequestPojo
argument_list|()
decl_stmt|;
name|input
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|input
argument_list|)
expr_stmt|;
specifier|final
name|ResponsePojo
name|response
init|=
operator|new
name|ResponsePojo
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|outJsonDataFormat
operator|.
name|unmarshal
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|any
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|AsyncCallback
argument_list|>
name|bindingCallback
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|processor
operator|.
name|process
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|bindingCallback
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|bindingProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|jsonDataFormat
argument_list|)
operator|.
name|marshal
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|same
argument_list|(
name|request
argument_list|)
argument_list|,
name|any
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bindingCallback
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|AsyncCallback
name|that
init|=
name|bindingCallback
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|that
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|response
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldMarshalAndUnmarshalXml ()
specifier|public
name|void
name|shouldMarshalAndUnmarshalXml
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|outType
init|=
name|ResponsePojo
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|RestProducerBindingProcessor
name|bindingProcessor
init|=
operator|new
name|RestProducerBindingProcessor
argument_list|(
name|processor
argument_list|,
name|context
argument_list|,
name|jsonDataFormat
argument_list|,
name|xmlDataFormat
argument_list|,
name|outJsonDataFormat
argument_list|,
name|outXmlDataFormat
argument_list|,
literal|"xml"
argument_list|,
literal|true
argument_list|,
name|outType
argument_list|)
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|input
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|RequestPojo
name|request
init|=
operator|new
name|RequestPojo
argument_list|()
decl_stmt|;
name|input
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|input
argument_list|)
expr_stmt|;
specifier|final
name|ResponsePojo
name|response
init|=
operator|new
name|ResponsePojo
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|outXmlDataFormat
operator|.
name|unmarshal
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|any
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|AsyncCallback
argument_list|>
name|bindingCallback
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|processor
operator|.
name|process
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|bindingCallback
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|bindingProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|xmlDataFormat
argument_list|)
operator|.
name|marshal
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|same
argument_list|(
name|request
argument_list|)
argument_list|,
name|any
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bindingCallback
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|AsyncCallback
name|that
init|=
name|bindingCallback
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|that
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|response
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotMarshalAndUnmarshalByDefault ()
specifier|public
name|void
name|shouldNotMarshalAndUnmarshalByDefault
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|outType
init|=
name|ResponsePojo
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|RestProducerBindingProcessor
name|bindingProcessor
init|=
operator|new
name|RestProducerBindingProcessor
argument_list|(
name|processor
argument_list|,
name|context
argument_list|,
name|jsonDataFormat
argument_list|,
name|xmlDataFormat
argument_list|,
name|outJsonDataFormat
argument_list|,
name|outXmlDataFormat
argument_list|,
literal|"off"
argument_list|,
literal|true
argument_list|,
name|outType
argument_list|)
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|input
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|RequestPojo
name|request
init|=
operator|new
name|RequestPojo
argument_list|()
decl_stmt|;
name|input
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|input
argument_list|)
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|AsyncCallback
argument_list|>
name|bindingCallback
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|processor
operator|.
name|process
argument_list|(
name|same
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|bindingCallback
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|bindingProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bindingCallback
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|AsyncCallback
name|that
init|=
name|bindingCallback
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|that
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

