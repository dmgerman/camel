begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|Book
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
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|BookStore
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
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|BookStoreImpl
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

begin_class
DECL|class|CamelRouterBuilder
specifier|public
class|class
name|CamelRouterBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|SOAP_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_ENDPOINT_URI
init|=
literal|"cxf://http://localhost:{{soapEndpointPort}}/soap"
operator|+
literal|"?serviceClass=org.apache.camel.example.cxf.jaxrs.resources.BookStore"
decl_stmt|;
DECL|field|REST_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|REST_ENDPOINT_URI
init|=
literal|"cxfrs://http://localhost:{{restEndpointPort}}/rest"
operator|+
literal|"?resourceClasses=org.apache.camel.example.cxf.jaxrs.resources.BookStoreImpl"
decl_stmt|;
comment|/**      * Allow this route to be run as an application      */
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"soapEndpointPort"
argument_list|,
literal|"9006"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"restEndpointPort"
argument_list|,
literal|"9002"
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|CamelRouterBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// JAXWSClient invocation
name|JAXWSClient
name|jaxwsClient
init|=
operator|new
name|JAXWSClient
argument_list|()
decl_stmt|;
name|BookStore
name|bookStore
init|=
name|jaxwsClient
operator|.
name|getBookStore
argument_list|()
decl_stmt|;
name|bookStore
operator|.
name|addBook
argument_list|(
operator|new
name|Book
argument_list|(
literal|"Camel User Guide"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|bookStore
operator|.
name|getBook
argument_list|(
literal|123L
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Get the book with id 123. "
operator|+
name|book
argument_list|)
expr_stmt|;
try|try
block|{
name|book
operator|=
name|bookStore
operator|.
name|getBook
argument_list|(
literal|124L
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Get the book with id 124. "
operator|+
name|book
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Expected exception received: "
operator|+
name|exception
argument_list|)
expr_stmt|;
block|}
comment|// JAXRSClient invocation
name|JAXRSClient
name|jaxrsClient
init|=
operator|new
name|JAXRSClient
argument_list|()
decl_stmt|;
name|bookStore
operator|=
name|jaxrsClient
operator|.
name|getBookStore
argument_list|()
expr_stmt|;
name|bookStore
operator|.
name|addBook
argument_list|(
operator|new
name|Book
argument_list|(
literal|"Karaf User Guide"
argument_list|,
literal|124L
argument_list|)
argument_list|)
expr_stmt|;
name|book
operator|=
name|bookStore
operator|.
name|getBook
argument_list|(
literal|124L
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Get the book with id 124. "
operator|+
name|book
argument_list|)
expr_stmt|;
try|try
block|{
name|book
operator|=
name|bookStore
operator|.
name|getBook
argument_list|(
literal|126L
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Get the book with id 126. "
operator|+
name|book
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Expected exception received: "
operator|+
name|exception
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
comment|// populate the message queue with some messages
name|from
argument_list|(
name|SOAP_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MappingProcessor
argument_list|(
operator|new
name|BookStoreImpl
argument_list|(
literal|false
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|REST_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MappingProcessor
argument_list|(
operator|new
name|BookStoreImpl
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Mapping the request to object's invocation
DECL|class|MappingProcessor
specifier|private
specifier|static
class|class
name|MappingProcessor
implements|implements
name|Processor
block|{
DECL|field|beanClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
decl_stmt|;
DECL|field|instance
specifier|private
name|Object
name|instance
decl_stmt|;
DECL|method|MappingProcessor (Object obj)
name|MappingProcessor
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|beanClass
operator|=
name|obj
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|instance
operator|=
name|obj
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
throws|throws
name|Exception
block|{
name|String
name|operationName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|findMethod
argument_list|(
name|operationName
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Object
name|response
init|=
name|method
operator|.
name|invoke
argument_list|(
name|instance
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
decl_stmt|;
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
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
DECL|method|findMethod (String operationName, Object[] parameters)
specifier|private
name|Method
name|findMethod
parameter_list|(
name|String
name|operationName
parameter_list|,
name|Object
index|[]
name|parameters
parameter_list|)
throws|throws
name|SecurityException
throws|,
name|NoSuchMethodException
block|{
return|return
name|beanClass
operator|.
name|getMethod
argument_list|(
name|operationName
argument_list|,
name|getParameterTypes
argument_list|(
name|parameters
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getParameterTypes (Object[] parameters)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|(
name|Object
index|[]
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Class
index|[
literal|0
index|]
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|answer
init|=
operator|new
name|Class
index|[
name|parameters
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|parameters
control|)
block|{
name|answer
index|[
name|i
index|]
operator|=
name|object
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

