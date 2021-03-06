begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|OffsetTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|SingleValueConverter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|DateTimeHandling
operator|.
name|ISO_OFFSET_TIME
import|;
end_import

begin_class
DECL|class|OffsetTimeConverter
specifier|final
class|class
name|OffsetTimeConverter
implements|implements
name|SingleValueConverter
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|SingleValueConverter
name|INSTANCE
init|=
operator|new
name|OffsetTimeConverter
argument_list|()
decl_stmt|;
DECL|method|OffsetTimeConverter ()
specifier|private
name|OffsetTimeConverter
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|canConvert (@uppressWarningsR) final Class type)
specifier|public
name|boolean
name|canConvert
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|Class
name|type
parameter_list|)
block|{
return|return
name|OffsetTime
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|fromString (final String value)
specifier|public
name|Object
name|fromString
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
name|OffsetTime
operator|.
name|parse
argument_list|(
name|value
argument_list|,
name|ISO_OFFSET_TIME
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString (final Object value)
specifier|public
name|String
name|toString
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|OffsetTime
name|offsetTime
init|=
operator|(
name|OffsetTime
operator|)
name|value
decl_stmt|;
return|return
name|ISO_OFFSET_TIME
operator|.
name|format
argument_list|(
name|offsetTime
argument_list|)
return|;
block|}
block|}
end_class

end_unit

