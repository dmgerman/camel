begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
comment|/**  * Time utils.  */
end_comment

begin_class
DECL|class|TimeUtils
specifier|public
specifier|final
class|class
name|TimeUtils
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
name|TimeUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NUMBERS_ONLY_STRING_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|NUMBERS_ONLY_STRING_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^[-]?(\\d)+$"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
DECL|field|HOUR_REGEX_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|HOUR_REGEX_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"((\\d)*(\\d))h(our(s)?)?"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
DECL|field|MINUTES_REGEX_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|MINUTES_REGEX_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"((\\d)*(\\d))m(in(ute(s)?)?)?"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
DECL|field|SECONDS_REGEX_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SECONDS_REGEX_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"((\\d)*(\\d))s(ec(ond)?(s)?)?"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
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
comment|// https://svn.apache.org/repos/asf/karaf/trunk/shell/commands/src/main/java/org/apache/karaf/shell/commands/impl/InfoAction.java
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
literal|" minute"
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
literal|24
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
literal|" hour"
operator|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
DECL|method|toMilliSeconds (String source)
specifier|public
specifier|static
name|long
name|toMilliSeconds
parameter_list|(
name|String
name|source
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// quick conversion if its only digits
name|boolean
name|digit
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|source
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|source
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// special for fist as it can be negative number
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
name|ch
operator|==
literal|'-'
condition|)
block|{
continue|continue;
block|}
comment|// quick check if its 0..9
if|if
condition|(
name|ch
argument_list|<
literal|'0'
operator|||
name|ch
argument_list|>
literal|'9'
condition|)
block|{
name|digit
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|digit
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|source
argument_list|)
return|;
block|}
name|long
name|milliseconds
init|=
literal|0
decl_stmt|;
name|boolean
name|foundFlag
init|=
literal|false
decl_stmt|;
name|checkCorrectnessOfPattern
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
decl_stmt|;
name|matcher
operator|=
name|createMatcher
argument_list|(
name|NUMBERS_ONLY_STRING_PATTERN
argument_list|,
name|source
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
comment|// Note: This will also be used for regular numeric strings.
comment|//       This String -> long converter will be used for all strings.
name|milliseconds
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|matcher
operator|=
name|createMatcher
argument_list|(
name|HOUR_REGEX_PATTERN
argument_list|,
name|source
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|milliseconds
operator|=
name|milliseconds
operator|+
operator|(
literal|3600000
operator|*
name|Long
operator|.
name|valueOf
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|)
expr_stmt|;
name|foundFlag
operator|=
literal|true
expr_stmt|;
block|}
name|matcher
operator|=
name|createMatcher
argument_list|(
name|MINUTES_REGEX_PATTERN
argument_list|,
name|source
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|long
name|minutes
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|minutes
operator|>
literal|59
operator|)
operator|&&
name|foundFlag
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Minutes should contain a valid value between 0 and 59: "
operator|+
name|source
argument_list|)
throw|;
block|}
name|foundFlag
operator|=
literal|true
expr_stmt|;
name|milliseconds
operator|=
name|milliseconds
operator|+
operator|(
literal|60000
operator|*
name|minutes
operator|)
expr_stmt|;
block|}
name|matcher
operator|=
name|createMatcher
argument_list|(
name|SECONDS_REGEX_PATTERN
argument_list|,
name|source
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|long
name|seconds
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|seconds
operator|>
literal|59
operator|)
operator|&&
name|foundFlag
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Seconds should contain a valid value between 0 and 59: "
operator|+
name|source
argument_list|)
throw|;
block|}
name|foundFlag
operator|=
literal|true
expr_stmt|;
name|milliseconds
operator|=
name|milliseconds
operator|+
operator|(
literal|1000
operator|*
name|seconds
operator|)
expr_stmt|;
block|}
comment|// No pattern matched... initiating fallback check and conversion (if required).
comment|// The source at this point may contain illegal values or special characters
if|if
condition|(
operator|!
name|foundFlag
condition|)
block|{
name|milliseconds
operator|=
name|Long
operator|.
name|valueOf
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"source: [{}], milliseconds: {}"
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
DECL|method|checkCorrectnessOfPattern (String source)
specifier|private
specifier|static
name|void
name|checkCorrectnessOfPattern
parameter_list|(
name|String
name|source
parameter_list|)
block|{
comment|//replace only numbers once
name|Matcher
name|matcher
init|=
name|createMatcher
argument_list|(
name|NUMBERS_ONLY_STRING_PATTERN
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|String
name|replaceSource
init|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
literal|""
argument_list|)
decl_stmt|;
comment|//replace hour string once
name|matcher
operator|=
name|createMatcher
argument_list|(
name|HOUR_REGEX_PATTERN
argument_list|,
name|replaceSource
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
operator|&&
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Hours should not be specified more then once: "
operator|+
name|source
argument_list|)
throw|;
block|}
name|replaceSource
operator|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|//replace minutes once
name|matcher
operator|=
name|createMatcher
argument_list|(
name|MINUTES_REGEX_PATTERN
argument_list|,
name|replaceSource
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
operator|&&
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Minutes should not be specified more then once: "
operator|+
name|source
argument_list|)
throw|;
block|}
name|replaceSource
operator|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|//replace seconds once
name|matcher
operator|=
name|createMatcher
argument_list|(
name|SECONDS_REGEX_PATTERN
argument_list|,
name|replaceSource
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
operator|&&
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Seconds should not be specified more then once: "
operator|+
name|source
argument_list|)
throw|;
block|}
name|replaceSource
operator|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|replaceSource
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal characters: "
operator|+
name|source
argument_list|)
throw|;
block|}
block|}
DECL|method|createMatcher (Pattern pattern, String source)
specifier|private
specifier|static
name|Matcher
name|createMatcher
parameter_list|(
name|Pattern
name|pattern
parameter_list|,
name|String
name|source
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|source
argument_list|)
return|;
block|}
block|}
end_class

end_unit

