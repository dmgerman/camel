begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|component
operator|.
name|jackson
operator|.
name|JacksonConstants
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
name|Test
import|;
end_import

begin_class
DECL|class|JacksonConversionsSimpleTest
specifier|public
class|class
name|JacksonConversionsSimpleTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// enable jackson type converter by setting this property on
comment|// CamelContext
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|JacksonConstants
operator|.
name|ENABLE_TYPE_CONVERTER
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertMapToString ()
specifier|public
name|void
name|shouldNotConvertMapToString
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Object
name|convertedObject
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
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|// will do a toString which is an empty map
name|assertEquals
argument_list|(
name|body
operator|.
name|toString
argument_list|()
argument_list|,
name|convertedObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertMapToNumber ()
specifier|public
name|void
name|shouldNotConvertMapToNumber
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Object
name|convertedObject
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|convertedObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertMapToPrimitive ()
specifier|public
name|void
name|shouldNotConvertMapToPrimitive
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Object
name|convertedObject
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|convertedObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertStringToEnum ()
specifier|public
name|void
name|shouldNotConvertStringToEnum
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Object
name|convertedObject
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|ExchangePattern
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
literal|"InOnly"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|convertedObject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

