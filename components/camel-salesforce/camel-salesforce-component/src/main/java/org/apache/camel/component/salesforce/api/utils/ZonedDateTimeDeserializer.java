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
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonDeserializer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|datatype
operator|.
name|jsr310
operator|.
name|deser
operator|.
name|InstantDeserializer
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
name|ISO_OFFSET_DATE_TIME
import|;
end_import

begin_class
DECL|class|ZonedDateTimeDeserializer
specifier|final
class|class
name|ZonedDateTimeDeserializer
extends|extends
name|InstantDeserializer
argument_list|<
name|ZonedDateTime
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|JsonDeserializer
argument_list|<
name|ZonedDateTime
argument_list|>
name|INSTANCE
init|=
operator|new
name|ZonedDateTimeDeserializer
argument_list|()
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|ZonedDateTimeDeserializer ()
specifier|private
name|ZonedDateTimeDeserializer
parameter_list|()
block|{
name|super
argument_list|(
name|InstantDeserializer
operator|.
name|ZONED_DATE_TIME
argument_list|,
name|ISO_OFFSET_DATE_TIME
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

