begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Converters for java.time.Duration.  * Provides a converter from a string (ISO-8601) to a Duration,  * a Duration to a string (ISO-8601) and  * a Duration to millis (long)  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|DurationConverter
specifier|public
specifier|final
class|class
name|DurationConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DurationConverter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|DurationConverter ()
specifier|private
name|DurationConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toMilliSeconds (Duration source)
specifier|public
specifier|static
name|long
name|toMilliSeconds
parameter_list|(
name|Duration
name|source
parameter_list|)
block|{
name|long
name|milliseconds
init|=
name|source
operator|.
name|toMillis
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"source: {} milliseconds: "
argument_list|,
name|source
argument_list|,
name|milliseconds
argument_list|)
expr_stmt|;
return|return
name|milliseconds
return|;
block|}
annotation|@
name|Converter
DECL|method|fromString (String source)
specifier|public
specifier|static
name|Duration
name|fromString
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|Duration
name|duration
init|=
name|Duration
operator|.
name|parse
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"source: {} milliseconds: "
argument_list|,
name|source
argument_list|,
name|duration
argument_list|)
expr_stmt|;
return|return
name|duration
return|;
block|}
annotation|@
name|Converter
DECL|method|asString (Duration source)
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|Duration
name|source
parameter_list|)
block|{
name|String
name|result
init|=
name|source
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"source: {} milliseconds: "
argument_list|,
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

