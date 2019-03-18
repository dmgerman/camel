begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ByteBuf
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|PooledByteBufAllocator
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
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
comment|/**  * Utility test to verify netty type converter.  */
end_comment

begin_class
DECL|class|NettyConverterTest
specifier|public
class|class
name|NettyConverterTest
extends|extends
name|CamelTestSupport
block|{
comment|/**      * Test payload to send.      */
DECL|field|PAYLOAD
specifier|private
specifier|static
specifier|final
name|String
name|PAYLOAD
init|=
literal|"Test Message"
decl_stmt|;
DECL|field|buf
specifier|private
name|ByteBuf
name|buf
decl_stmt|;
annotation|@
name|Before
DECL|method|startUp ()
specifier|public
name|void
name|startUp
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|PAYLOAD
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|buf
operator|=
name|PooledByteBufAllocator
operator|.
name|DEFAULT
operator|.
name|buffer
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|buf
operator|.
name|writeBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|buf
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConversionWithExchange ()
specifier|public
name|void
name|testConversionWithExchange
parameter_list|()
block|{
name|String
name|result
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|,
name|buf
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PAYLOAD
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConversionWithoutExchange ()
specifier|public
name|void
name|testConversionWithoutExchange
parameter_list|()
block|{
name|String
name|result
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|buf
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PAYLOAD
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

