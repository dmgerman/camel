begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormatSymbols
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * Time utils.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TimeUtils
specifier|public
specifier|final
class|class
name|TimeUtils
block|{
DECL|method|TimeUtils ()
specifier|private
name|TimeUtils
parameter_list|()
block|{     }
comment|/**      * Prints the duration in a human readable format as X days Y hours Z minutes etc.      *      * @param uptime the uptime in millis      * @return the time used for displaying on screen or in logs      */
DECL|method|printDuration (double uptime)
specifier|public
specifier|static
name|String
name|printDuration
parameter_list|(
name|double
name|uptime
parameter_list|)
block|{
comment|// Code taken from Karaf
comment|// https://svn.apache.org/repos/asf/felix/trunk/karaf/shell/commands/src/main/java/org/apache/felix/karaf/shell/commands/InfoAction.java
name|NumberFormat
name|fmtI
init|=
operator|new
name|DecimalFormat
argument_list|(
literal|"###,###"
argument_list|,
operator|new
name|DecimalFormatSymbols
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
decl_stmt|;
name|NumberFormat
name|fmtD
init|=
operator|new
name|DecimalFormat
argument_list|(
literal|"###,##0.000"
argument_list|,
operator|new
name|DecimalFormatSymbols
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
decl_stmt|;
name|uptime
operator|/=
literal|1000
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|60
condition|)
block|{
return|return
name|fmtD
operator|.
name|format
argument_list|(
name|uptime
argument_list|)
operator|+
literal|" seconds"
return|;
block|}
name|uptime
operator|/=
literal|60
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|60
condition|)
block|{
name|long
name|minutes
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|minutes
argument_list|)
operator|+
operator|(
name|minutes
operator|>
literal|1
condition|?
literal|" minutes"
else|:
literal|" minute"
operator|)
decl_stmt|;
return|return
name|s
return|;
block|}
name|uptime
operator|/=
literal|60
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|24
condition|)
block|{
name|long
name|hours
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|long
name|minutes
init|=
call|(
name|long
call|)
argument_list|(
operator|(
name|uptime
operator|-
name|hours
operator|)
operator|*
literal|60
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|hours
argument_list|)
operator|+
operator|(
name|hours
operator|>
literal|1
condition|?
literal|" hours"
else|:
literal|" hour"
operator|)
decl_stmt|;
if|if
condition|(
name|minutes
operator|!=
literal|0
condition|)
block|{
name|s
operator|+=
literal|" "
operator|+
name|fmtI
operator|.
name|format
argument_list|(
name|minutes
argument_list|)
operator|+
operator|(
name|minutes
operator|>
literal|1
condition|?
literal|" minutes"
else|:
literal|"minute"
operator|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
name|uptime
operator|/=
literal|24
expr_stmt|;
name|long
name|days
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|long
name|hours
init|=
call|(
name|long
call|)
argument_list|(
operator|(
name|uptime
operator|-
name|days
operator|)
operator|*
literal|60
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|days
argument_list|)
operator|+
operator|(
name|days
operator|>
literal|1
condition|?
literal|" days"
else|:
literal|" day"
operator|)
decl_stmt|;
if|if
condition|(
name|hours
operator|!=
literal|0
condition|)
block|{
name|s
operator|+=
literal|" "
operator|+
name|fmtI
operator|.
name|format
argument_list|(
name|hours
argument_list|)
operator|+
operator|(
name|hours
operator|>
literal|1
condition|?
literal|" hours"
else|:
literal|"hour"
operator|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
block|}
end_class

end_unit

