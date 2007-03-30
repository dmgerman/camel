begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|TypeConverter
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
name|util
operator|.
name|CamelClient
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|bus
operator|.
name|CXFBusFactory
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
name|Message
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
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|DestinationFactory
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
name|transport
operator|.
name|DestinationFactoryManager
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
name|transport
operator|.
name|MessageObserver
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
name|transport
operator|.
name|local
operator|.
name|LocalConduit
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xmlsoap
operator|.
name|schemas
operator|.
name|wsdl
operator|.
name|http
operator|.
name|AddressType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfTest
specifier|public
class|class
name|CxfTest
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CxfTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|client
specifier|protected
name|CamelClient
name|client
init|=
operator|new
name|CamelClient
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
DECL|method|testInvokeOfServer ()
specifier|public
name|void
name|testInvokeOfServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets register a service
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|(
literal|null
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|)
decl_stmt|;
name|AddressType
name|a
init|=
operator|new
name|AddressType
argument_list|()
decl_stmt|;
name|a
operator|.
name|setLocation
argument_list|(
literal|"http://localhost/test"
argument_list|)
expr_stmt|;
name|ei
operator|.
name|addExtensor
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|CXFBusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|DestinationFactory
name|factory
init|=
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
decl_stmt|;
name|Destination
name|destination
init|=
name|factory
operator|.
name|getDestination
argument_list|(
name|ei
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|EchoObserver
argument_list|()
argument_list|)
expr_stmt|;
comment|// now lets invoke it via Camel
name|CxfExchange
name|exchange
init|=
operator|(
name|CxfExchange
operator|)
name|client
operator|.
name|send
argument_list|(
literal|"cxf:http://localhost/test"
argument_list|,
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"requestHeader"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world</hello>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|Message
name|cxfOutMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received output message: "
operator|+
name|out
operator|+
literal|" and CXF out: "
operator|+
name|cxfOutMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"replyHeader on CXF"
argument_list|,
literal|"foo2"
argument_list|,
name|cxfOutMessage
operator|.
name|get
argument_list|(
literal|"replyHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"replyHeader on Camel"
argument_list|,
literal|"foo2"
argument_list|,
name|out
operator|.
name|getHeader
argument_list|(
literal|"replyHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|output
argument_list|)
expr_stmt|;
block|}
DECL|class|EchoObserver
specifier|protected
class|class
name|EchoObserver
implements|implements
name|MessageObserver
block|{
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Received message: "
operator|+
name|message
operator|+
literal|" with content types: "
operator|+
name|message
operator|.
name|getContentFormats
argument_list|()
argument_list|)
expr_stmt|;
name|Conduit
name|backChannel
init|=
name|message
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|message
operator|.
name|remove
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|)
expr_stmt|;
name|TypeConverter
name|converter
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|String
name|request
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Request body: "
operator|+
name|request
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|MessageImpl
name|reply
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|reply
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo header"
argument_list|,
literal|"bar"
argument_list|,
name|reply
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|reply
operator|.
name|put
argument_list|(
literal|"replyHeader"
argument_list|,
name|message
operator|.
name|get
argument_list|(
literal|"requestHeader"
argument_list|)
operator|+
literal|"2"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|reply
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"entrySet.size()"
argument_list|,
literal|2
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//reply.setContent(String.class, "<reply>true</reply>");
name|InputStream
name|payload
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<reply>true</reply>"
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"sending reply: "
operator|+
name|reply
argument_list|)
expr_stmt|;
name|backChannel
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|/*                 backChannel.send(message);                  OutputStream out = message.getContent(OutputStream.class);                 InputStream in = message.getContent(InputStream.class);                  copy(in, out, 1024);                  out.close();                 in.close(); */
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|copy (final InputStream input, final OutputStream output, final int bufferSize)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
specifier|final
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

