begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ical
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ical
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|DateProperty
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
name|Converter
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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * ICal related converter.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|ICalConverter
specifier|public
specifier|final
class|class
name|ICalConverter
block|{
DECL|method|ICalConverter ()
specifier|private
name|ICalConverter
parameter_list|()
block|{
comment|// Helper class
block|}
annotation|@
name|Converter
DECL|method|toDate (DateProperty property)
specifier|public
specifier|static
name|Date
name|toDate
parameter_list|(
name|DateProperty
name|property
parameter_list|)
block|{
return|return
name|property
operator|.
name|getDate
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toStream (Calendar calendar, Exchange exchange)
specifier|public
specifier|static
name|ByteArrayInputStream
name|toStream
parameter_list|(
name|Calendar
name|calendar
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|calendar
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

