begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|HttpTransceiver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|specific
operator|.
name|SpecificRequestor
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
name|avro
operator|.
name|generated
operator|.
name|KeyValueProtocol
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
name|avro
operator|.
name|processors
operator|.
name|GetProcessor
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
name|avro
operator|.
name|processors
operator|.
name|PutProcessor
import|;
end_import

begin_class
DECL|class|AvroHttpConsumerTest
specifier|public
class|class
name|AvroHttpConsumerTest
extends|extends
name|AvroConsumerTestSupport
block|{
annotation|@
name|Override
DECL|method|initializeTranceiver ()
specifier|protected
name|void
name|initializeTranceiver
parameter_list|()
throws|throws
name|IOException
block|{
name|transceiver
operator|=
operator|new
name|HttpTransceiver
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|avroPort
argument_list|)
argument_list|)
expr_stmt|;
name|requestor
operator|=
operator|new
name|SpecificRequestor
argument_list|(
name|KeyValueProtocol
operator|.
name|class
argument_list|,
name|transceiver
argument_list|)
expr_stmt|;
block|}
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
comment|//In Only
name|from
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPort
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|el
argument_list|(
literal|"${in.headers."
operator|+
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
operator|+
literal|" == 'put'}"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|PutProcessor
argument_list|(
name|keyValue
argument_list|)
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|el
argument_list|(
literal|"${in.headers."
operator|+
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
operator|+
literal|" == 'get'}"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|GetProcessor
argument_list|(
name|keyValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

